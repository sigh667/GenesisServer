package com.mokylin.bleach.gameserver.shop.shop;

import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.common.shop.template.ShopTemplate;
import com.mokylin.bleach.gamedb.orm.entity.ShopEntity;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.persistance.ObjectInSqlImpl;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.ShopMessage.GoodInfo;

/**
 * 商店
 * @author yaguang.xiao
 *
 */
public class Shop extends ObjectInSqlImpl<Long, ShopEntity> {

	/** 商店UUId */
	private long id;
	/** 玩家角色 */
	private Human human;
	/** 商店模板 */
	private ShopTemplate shopTemplate;
	
	/** 货物刷出时间 */
	private long goodBornTime;
	/** 货物列表 */
	private List<Good> goodList = Lists.newArrayList();
	
	/** 商店刷新 */
	private ShopRefresh shopRefresh;
	
	// 商店开启关闭相关数据
	/** 本商店是否是永久开启 */
	private boolean isOpenForever;
	/** 商店关闭时间（商店不是永久开启时此字段才有效） */
	private Timestamp closeTime;
	/** 商店是否开启 */
	private boolean isOpen;
	
	/**
	 * 用于从数据库中创建商店对象
	 * @param human
	 */
	public Shop(Human human) {
		super(human.getDataUpdater());
		this.human = human;
	}
	
	/**
	 * 用于第一次创建商店对象
	 * @param human
	 * @param id
	 * @param shopType
	 * @param isOpenForever
	 * @param closeTime
	 */
	public Shop(Human human, long id, ShopType shopType, Timestamp closeTime) {
		this(human);
		this.id = id;
		this.shopTemplate = GlobalData.getTemplateService().get(shopType.getIndex(), ShopTemplate.class);
		this.shopRefresh = new ShopRefresh(this, human);
		this.isOpenForever = shopType.isOpenForever();
		this.closeTime = closeTime;
		this.isOpen = true;
	}

	@Override
	public Long getDbId() {
		return this.id;
	}

	@Override
	public ShopEntity toEntity() {
		ShopEntity entity = new ShopEntity();
		entity.setId(id);
		entity.setHumanId(this.human.getId());
		entity.setShopType(this.getShopType().getIndex());
		entity.setGoodBornTime(this.goodBornTime);
		entity.setGoodDataList(JSON.toJSONString(GoodData.createGoodDataList(this.goodList)));
		entity.setManuallyRefreshCount(this.shopRefresh.getMauallyRefresh().getManuallyRefreshCount());
		entity.setLastResetManuallyRefreshCountTime(new Timestamp(this.shopRefresh.getMauallyRefresh().getLastExecuteTime()));
		entity.setLastAutoRefreshTime(new Timestamp(this.shopRefresh.getAutoRefresh().getLastExecuteTime()));
		entity.setIsOpenForever(this.isOpenForever ? 1 : 0);
		entity.setCloseTime(closeTime);
		return entity;
	}

	@Override
	public void fromEntity(ShopEntity entity) {
		this.id = entity.getId();
		this.shopTemplate = GlobalData.getTemplateService().get(entity.getShopType(), ShopTemplate.class);
		this.goodBornTime = entity.getGoodBornTime();
		this.goodList = Good.createGoodList(JSON.parseArray(entity.getGoodDataList(), GoodData.class), this);
		this.shopRefresh = new ShopRefresh(this, human, entity.getManuallyRefreshCount(), entity.getLastResetManuallyRefreshCountTime(),
				entity.getLastAutoRefreshTime());
		this.isOpenForever = entity.getIsOpenForever() == 1 ? true : false;
		this.closeTime = entity.getCloseTime();
		if(this.isOpenForever || this.closeTime.getTime() > Globals.getTimeService().now()) {
			this.isOpen = true;
		} else {
			this.isOpen = false;
		}
	}
	
	public ShopType getShopType() {
		return this.shopTemplate.getShopType();
	}

	public ShopTemplate getShopTemplate() {
		return shopTemplate;
	}

	/**
	 * 获取关闭时间
	 * @return
	 */
	public long getCloseTime() {
		if(this.closeTime == null) {
			return 0;
		}
		
		return this.closeTime.getTime();
	}
	
	/**
	 * 打开商店
	 */
	public void open() {
		this.isOpen = true;
	}
	
	/**
	 * 设置关闭时间
	 * @param lastMinutes
	 */
	public void setDeadline(int lastMinutes) {
		this.closeTime = new Timestamp(Globals.getTimeService().now() + lastMinutes * 60 * 1000);
	}
	
	/**
	 * 获取剩余的开放时间（秒）
	 * @return
	 */
	public int getLeftOpenTime() {
		if(!this.isOpen || this.closeTime == null) {
			return 0;
		}
		
		long leftOpenTime = this.closeTime.getTime() - Globals.getTimeService().now();
		if(leftOpenTime < 0) {
			return 0;
		}
		
		return (int) (leftOpenTime / 1000);
	}

	public boolean isOpen() {
		return this.isOpen;
	}
	
	/**
	 * 关闭商店
	 */
	public void close() {
		this.isOpen = false;
	}
	
	public boolean isOpenForever() {
		return isOpenForever;
	}
	
	/**
	 * 是否已经刷新过
	 * @param bornTime
	 * @return
	 */
	public boolean isRefreshed(long bornTime) {
		return this.goodBornTime != bornTime;
	}
	
	/**
	 * 获取货物
	 * @param position
	 * @return
	 */
	public Good getGood(int position) {
		if(position < 0 || position >= this.goodList.size()) {
			return null;
		}
		
		return this.goodList.get(position);
	}

	/**
	 * 打开商店时候的钩子方法
	 * @param humanLevel
	 */
	public void onOpenShop(Human human) {
		this.shopRefresh.getMauallyRefresh().triggerExecute(human);
		this.shopRefresh.getAutoRefresh().triggerExecute(human);
	}
	
	/**
	 * 获取商店刷新对象
	 * @return
	 */
	public ShopRefresh getShopRefresh() {
		return this.shopRefresh;
	}
	
	public long getGoodBornTime() {
		return this.goodBornTime;
	}
	
	/**
	 * 设置商店物品列表
	 * @param goodList
	 */
	void setGoodList(List<Good> goodList, long goodBornTime) {
		this.goodList = goodList;
		this.goodBornTime = goodBornTime;
	}
	
	/**
	 * 构建商品信息列表
	 * @return
	 */
	public List<GoodInfo> buildGoodInfoList() {
		List<GoodInfo> goodInfoList = Lists.newArrayListWithCapacity(this.goodList.size());
		for(Good good : this.goodList) {
			goodInfoList.add(good.toGoodInfo());
		}
		
		return goodInfoList;
	}
	
}
