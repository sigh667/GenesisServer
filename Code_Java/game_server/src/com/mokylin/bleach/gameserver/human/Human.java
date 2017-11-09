package com.mokylin.bleach.gameserver.human;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.common.exp.ExpData;
import com.mokylin.bleach.common.human.HumanPropContainer;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.common.human.template.ExpTemplate;
import com.mokylin.bleach.common.prop.IPropNotifier;
import com.mokylin.bleach.common.prop.battleprop.notify.PropsToNotify;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.core.util.MathUtils;
import com.mokylin.bleach.gamedb.human.HumanData;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.persistance.DataUpdater;
import com.mokylin.bleach.gameserver.core.persistance.IDataUpdater;
import com.mokylin.bleach.gameserver.core.persistance.ObjectInSqlImpl;
import com.mokylin.bleach.gameserver.currency.CurrencyManager;
import com.mokylin.bleach.gameserver.dailyreward.DailyRewardManager;
import com.mokylin.bleach.gameserver.function.FunctionManager;
import com.mokylin.bleach.gameserver.hero.HeroManager;
import com.mokylin.bleach.gameserver.human.energy.EnergyManager;
import com.mokylin.bleach.gameserver.human.event.HumanLevelUpEvent;
import com.mokylin.bleach.gameserver.human.event.HumanLevelUpReason;
import com.mokylin.bleach.gameserver.item.Inventory;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.shop.ShopManager;
import com.mokylin.bleach.gameserver.window.WindowManager;
import com.mokylin.bleach.protobuf.HeroMessage.GCChangedProps;
import com.mokylin.bleach.protobuf.HeroMessage.PropData;
import com.mokylin.bleach.protobuf.HumanMessage.GCDataError;
import com.mokylin.bleach.protobuf.HumanMessage.GCHumanDetailInfo;
import com.mokylin.bleach.protobuf.HumanMessage.GCHumanDetailInfo.Builder;
import com.mokylin.bleach.protobuf.HumanMessage.GCHumanLevelUp;

/**
 * 角色
 * @author baoliang.shen
 *
 */
public class Human extends ObjectInSqlImpl<Long, HumanEntity> implements MsgArgs, IPropNotifier{
	private static final Logger log = LoggerFactory.getLogger(Human.class);
	
	/** 玩家的最低等级 */
	public static final int MIN_LEVEL = 1;
	
	private final ServerGlobals sGlobals;

	private final Player player;
	
	/**角色ID*/
	private Long id;
	/**渠道*/
	private String channel;
	/**所属的账号ID*/
	private String accountId;
	/**初始服务器ID*/
	private int originalServerId;
	/**当前服务器ID*/
	private int currentServerId;
	/**名字*/
	private String name;
	/**创建时间*/
	private Timestamp createTime;
	/** 登陆时间 */
	private final Timestamp loginTime;
	/** 上次humanEntity保存时间，初始为登陆时间 */
	private long lastToEntityTime;
	/** 总在线时长 */
	private long totalOnlineTime;
	
	/**玩家时间轴*/
	public final TimeAxis<Human> timeAxis = new TimeAxis<Human>(Globals.getTimeService(), this);

	/** 属性容器 */
	private HumanPropContainer propContaniner = new HumanPropContainer();
	
	/** 体力管理器 */
	private EnergyManager energyManager = new EnergyManager(this);
	

	/** 金钱管理器 */
	private CurrencyManager currencyManager = new CurrencyManager(this);

	/**背包*/
	private Inventory inventory = new Inventory(this);
	/***/
	private HeroManager heroManager = new HeroManager(this);
	/** 每日奖励管理器 */
	private DailyRewardManager dailyRewardManager = new DailyRewardManager(this);
	/** 商店管理器 */
	private ShopManager shopManager = new ShopManager(this);
	/** 窗口管理器 */
	private WindowManager windowManager = new WindowManager();
	/** 功能管理器 */
	private FunctionManager funcManager = new FunctionManager(this);

	/**
	 * 创建角色时，使用的构造函数
	 * @param humanInfo
	 * @param dataUpdater 
	 * @param sGlobals 
	 */
	public Human(HumanInfo humanInfo, Player player, IDataUpdater dataUpdater, ServerGlobals sGlobals) {
		super(dataUpdater);
		this.sGlobals = sGlobals;
		this.player = player;
		//初始化humanInfo指定的值
		this.id = humanInfo.getId();
		this.accountId = humanInfo.getAccountId();
		this.channel = humanInfo.getChannel();
		this.name = humanInfo.getName();
		this.originalServerId = humanInfo.getOriginalServerId();

		//初始化程序指定的数据
		long now = Globals.getTimeService().now();
		this.createTime = new Timestamp(now);
		this.currentServerId = humanInfo.getOriginalServerId();
		
		this.loginTime = new Timestamp(now);
		this.lastToEntityTime = now;
	}
	
	/**
	 * 新创建的角色才调用此方法
	 */
	public void initAfterNewCreate() {
		//初始化策划指定的数据
		propContaniner.set(HumanPropId.LEVEL, Human.MIN_LEVEL);
		heroManager.giveInitialHeros();
		dailyRewardManager.giveHeroWhenNewCreateIfNecessary();
		this.funcManager.initWhenNewCreateHuman();
		this.shopManager.initWhenNewCreateHuman();
		//初始化体力相关数据
		this.energyManager.initWhenNewCreateHuman();
		
	}
	
	/**
	 * 加载已有角色时，使用的构造函数
	 * @param humanData
	 * @param dataUpdater 
	 * @param sGlobals 
	 */
	public Human(HumanData humanData, Player player, DataUpdater dataUpdater, ServerGlobals sGlobals) {
		super(dataUpdater);
		
		long now = Globals.getTimeService().now();
		this.loginTime = new Timestamp(now);
		this.lastToEntityTime = now;
		
		this.sGlobals = sGlobals;
		this.player = player;
		this.fromEntity(humanData.humanEntity);

		inventory.loadFromEntity(humanData.itemEntityList);
		heroManager.loadFromEntity(humanData.heroEntityList);
		shopManager.loadFromEntity(humanData.shopEntityList);
		funcManager.loadFromEntities(humanData.functionEntityList);
	}

	/**
	 * 计算总在线时间
	 */
	private void computeTotalOnlineTime() {
		long now = Globals.getTimeService().now();
		totalOnlineTime += now - this.lastToEntityTime;
		this.lastToEntityTime = now;
	}
	
	@Override
	public Long getDbId() {
		return id;
	}
	@Override
	public HumanEntity toEntity() {
		HumanEntity humanEntity = new HumanEntity();
		humanEntity.setChannel(channel);
		humanEntity.setAccountId(accountId);
		humanEntity.setCreateTime(createTime);
		humanEntity.setLoginTime(loginTime);
		
		this.computeTotalOnlineTime();
		humanEntity.setTotalOnlineTime(totalOnlineTime);
		
		humanEntity.setCurrentServerId(currentServerId);
		humanEntity.setId(id);
		humanEntity.setLevel(propContaniner.get(HumanPropId.LEVEL));
		humanEntity.setExp(this.propContaniner.get(HumanPropId.EXP));
		humanEntity.setVipLevel(propContaniner.get(HumanPropId.VIP_LEVEL));
		humanEntity.setVipExp(this.propContaniner.get(HumanPropId.VIP_EXP));
		humanEntity.setName(name);
		humanEntity.setOriginalServerId(originalServerId);
		humanEntity.setAccumulatedChargeDiamond(this.currencyManager.get(CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND));
		humanEntity.setAccumulatedConsumedChargeDiamond(this.currencyManager.get(CurrencyPropId.ACCUMULATED_CONSUMED_CHARGE_DIAMOND));
		humanEntity.setChargeDiamond(this.currencyManager.get(CurrencyPropId.CHARGE_DIAMOND));
		humanEntity.setFreeDiamond(this.currencyManager.get(CurrencyPropId.FREE_DIAMOND));
		humanEntity.setGold(this.currencyManager.get(CurrencyPropId.GOLD));
		humanEntity.setTimesOfDailyReward(dailyRewardManager.getTimesOfReward());
		humanEntity.setLastDailyRewardTime(new Timestamp(dailyRewardManager.getLastRewardTime()));
		humanEntity.setEnergy(this.propContaniner.get(HumanPropId.ENERGY));
		humanEntity.setLastEnergyRecoverTime(new Timestamp(this.propContaniner.get(HumanPropId.LAST_ENERGY_RECOVER_TIME)));
		humanEntity.setBuyEnergyCounts(this.propContaniner.get(HumanPropId.BUY_ENERGY_COUNTS));
		humanEntity.setLastBuyEnergyCountsResetTime(new Timestamp(propContaniner.get(HumanPropId.LAST_BUY_ENERGY_COUNTS_RESET_TIME)));
		return humanEntity;
	}
	@Override
	public void fromEntity(HumanEntity entity) {
		this.channel = entity.getChannel();
		this.accountId = entity.getAccountId();
		this.createTime = entity.getCreateTime();
		this.totalOnlineTime = entity.getTotalOnlineTime();
		this.currentServerId = entity.getCurrentServerId();
		this.id = entity.getId();
		this.name = entity.getName();
		this.originalServerId = entity.getOriginalServerId();
		if (entity.getChargeDiamond() < 0
				|| entity.getFreeDiamond() < 0
				|| MathUtils.longAddOverflow(entity.getChargeDiamond(),
						entity.getFreeDiamond())) {
			throw new RuntimeException("充值钻石和免费钻石数据错误：充值钻石[" + entity.getChargeDiamond() + "], 免费钻石[" + entity.getFreeDiamond() + "]");
		}
		this.currencyManager.set(CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND, entity.getAccumulatedChargeDiamond());
		this.currencyManager.set(CurrencyPropId.ACCUMULATED_CONSUMED_CHARGE_DIAMOND, entity.getAccumulatedConsumedChargeDiamond());
		this.currencyManager.set(CurrencyPropId.CHARGE_DIAMOND, entity.getChargeDiamond());
		this.currencyManager.set(CurrencyPropId.FREE_DIAMOND, entity.getFreeDiamond());
		this.currencyManager.set(CurrencyPropId.GOLD, entity.getGold());

		this.propContaniner.set(HumanPropId.LEVEL, entity.getLevel());
		this.propContaniner.set(HumanPropId.EXP, entity.getExp());
		this.propContaniner.set(HumanPropId.VIP_LEVEL, entity.getVipLevel());
		this.propContaniner.set(HumanPropId.VIP_EXP, entity.getVipExp());
		this.propContaniner.set(HumanPropId.ENERGY, entity.getEnergy());
		this.propContaniner.set(HumanPropId.LAST_ENERGY_RECOVER_TIME, entity.getLastEnergyRecoverTime().getTime());
		this.propContaniner.set(HumanPropId.BUY_ENERGY_COUNTS, entity.getBuyEnergyCounts());
		this.propContaniner.set(HumanPropId.LAST_BUY_ENERGY_COUNTS_RESET_TIME, entity.getLastBuyEnergyCountsResetTime().getTime());
		
		dailyRewardManager.setTimesOfReward(entity.getTimesOfDailyReward());
		dailyRewardManager.setLastRewardTime(entity.getLastDailyRewardTime().getTime());
	}
	
	/**
	 * 登录时初始化整个Human用，位置在PlayerActor的initHuman方法。
	 */
	public void init(){
		dailyRewardManager.init();
		this.shopManager.init();
		energyManager.init();
	}
	
	/**
	 * 登录时，给客户端发角色所有信息，在完成init方法之后执行。
	 */
	public void notifyOnLogin() {
		GCHumanDetailInfo msg = this.buildDetailInfo();
		this.sendMessage(msg);
		
		inventory.notifyOnLogin();
		heroManager.notifyOnLogin();
		dailyRewardManager.sendDailyRewardInfo();
		this.funcManager.notifyOnLogin();
		this.shopManager.notifyOnLogin();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public int getOriginalServerId() {
		return originalServerId;
	}
	public void setOriginalServerId(int originalServerId) {
		this.originalServerId = originalServerId;
	}
	public int getCurrentServerId() {
		return currentServerId;
	}
	public void setCurrentServerId(int currentServerId) {
		this.currentServerId = currentServerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getLoginTime() {
		return loginTime;
	}
	/**
	 * 获取玩家总在线时间
	 * @return
	 */
	public long getTotalOnlineTime() {
		this.computeTotalOnlineTime();
		return this.totalOnlineTime;
	}

	/**
	 * 获取玩家的指定的属性值
	 * @param id	属性类型
	 * @return
	 */
	public long get(HumanPropId id) {
		return this.propContaniner.get(id);
	}
	
	/**
	 * 获取玩家的指定属性的int值，如果该值超过Int的描述范围，则返回[+/-]2147483647
	 * 
	 * @param id	属性类型
	 * @return
	 */
	public int getInt(HumanPropId id) {
		long tmpLong = this.propContaniner.get(id);
		
		if ( Math.abs(tmpLong) > Integer.MAX_VALUE) {
			log.warn("玩家属性ToInt溢出：玩家ID【{}】，属性【{}={}】", getDbId(), id, tmpLong);
			return Long.signum(tmpLong) * Integer.MAX_VALUE;
		}
		return (int)tmpLong;
	}
	
	/**
	 * 设置玩家指定的属性值
	 * @param id
	 * @param value
	 * @return
	 */
	public void set(HumanPropId id, long value) {
		if(id == null)
			return;
		
		this.propContaniner.set(id, value);
	}
	
	/**
	 * 获取累计充值钻石数量
	 * @return
	 */
	public long getAccumulatedChargeDiamond() {
		return this.currencyManager.get(CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND);
	}

	/**
	 * 给钱
	 * @param currencyPropId 金钱类型
	 * @param addValue	金钱数量
	 */
	public void giveMoney(CurrencyPropId currencyPropId, long addValue) {
		this.currencyManager.giveMoney(currencyPropId, addValue);
	}
	
	/**
	 * 判断货币是否够用
	 * @param currency	货币类型
	 * @param value	货币数量
	 * @return
	 */
	public boolean isMoneyEnough(Currency currency, long value) {
		return this.currencyManager.isMoneyEnough(currency, value);
	}

	/**
	 * 扣钱
	 * @param currency	货币类型
	 * @param value
	 */
	public void costMoney(Currency currency, long value) {
		this.currencyManager.costMoney(currency, value);
	}

	/**
	 * 给玩家发送消息
	 * @param msg
	 */
	public void sendMessage(GeneratedMessage msg) {
		this.player.sendMessage(msg);
	}
	
	/**
	 * 给玩家发送消息
	 * @param msg
	 */
	public <T extends GeneratedMessage.Builder<?>> void sendMessage(GeneratedMessage.Builder<T> msg) {
		this.player.sendMessage(msg);
	}

	/**
	 * 构建Human详细消息
	 * @return
	 */
	public GCHumanDetailInfo buildDetailInfo() {
		Builder builder = GCHumanDetailInfo.newBuilder();
		builder.setId(this.getId());
		builder.setName(this.getName());
		builder.setAccountId(this.getAccountId());
		builder.setChannel(this.channel);

		builder.addAllLongProp(propContaniner.values());
		builder.addAllCurrencyProp(currencyManager.getCurrencyValues());

		return builder.build();
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	public HeroManager getHeroManager() {
		return heroManager;
	}
	public ServerGlobals getServerGlobals() {
		return sGlobals;
	}
	public DailyRewardManager getDailyRewardManager() {
		return dailyRewardManager;
	}
	public ShopManager getShopManager() {
		return this.shopManager;
	}
	public WindowManager getWindowManager() {
		return this.windowManager;
	}
	public FunctionManager getFuncManager() {
		return this.funcManager;
	}
	public HumanPropContainer getPropContaniner() {
		return propContaniner;
	}
	public EnergyManager getEnergyManager() {
		return energyManager;
	}
	public CurrencyManager getCurrencyManager() {
		return currencyManager;
	}

	public void notifyDataErrorAndDisconnect() {
		//通知客户端，你的数据不正确
		GCDataError msg = GCDataError.newBuilder().build();
		player.sendMessage(msg);
		
		//将客户端踢下线
		player.disconnect();
	}

	@Override
	public void notifyPropChanges(ArrayList<PropsToNotify> list) {
		GCChangedProps.Builder changedPropsB = GCChangedProps.newBuilder();
		PropData.Builder propB =  PropData.newBuilder();
		
		for (PropsToNotify propsToNotify : list) {
			propB.setPropId(propsToNotify.getPropId());
			propB.setValue(propsToNotify.getValue());
			changedPropsB.addChangedProps(propB);
		}
		
		sendMessage(changedPropsB.build());
	}
	
	public TimeAxis<Human> getTimeAxis() {
		return this.timeAxis;
	}
	
	/**
	 * 判断是否可以增加经验
	 */
	public boolean isExpAddable(){
		return Globals.getExpService().isAddable(this.get(HumanPropId.LEVEL),
				GlobalData.getConstants().getMaxLevel(),
				this.get(HumanPropId.EXP),ExpTemplate.ExpEnum.HUMAN);
	}
	
	/**
	 * 增加经验
	 * 
	 * @param addExp  增加的经验值
	 * @param reason  增加经验的原因：打怪，使用药品
	 */
	public void addExp(long addExp, HumanLevelUpReason reason) {
		final int ORIG_LEVEL = this.getInt(HumanPropId.LEVEL);
		ExpData exp = Globals.getExpService().addExp(
				this.get(HumanPropId.LEVEL),
				GlobalData.getConstants().getMaxLevel(),
				this.get(HumanPropId.EXP), addExp,
				ExpTemplate.ExpEnum.HUMAN);
		this.set(HumanPropId.LEVEL, exp.getLevel());
		this.set(HumanPropId.EXP, exp.getExp());
		this.setModified();
		
		if (exp.getLevel() > ORIG_LEVEL) {
			Globals.getEventBus().occurs(new HumanLevelUpEvent(this, reason, ORIG_LEVEL));
		}
		this.sendMessage(GCHumanLevelUp.newBuilder().setLevel(exp.getLevel()).setExp(exp.getExp()));
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
