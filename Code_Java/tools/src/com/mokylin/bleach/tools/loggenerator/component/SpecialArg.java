package com.mokylin.bleach.tools.loggenerator.component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.player.Player;

/**
 * 特殊参数<p>
 * 这是一个非常重要的枚举，用来处理通用字段<p>
 * 比如玩家通用数据：玩家Id，玩家名字，玩家等级，玩家渠道/服务器全局数据：服务器Id<p>
 * 
 * 在这里面配置大的字段枚举都会进行特殊处理
 * 
 * @author yaguang.xiao
 *
 */
public enum SpecialArg {
	iWorldId(ServerGlobals.class, "serverGlobals.getServerId()"), //游戏大区ID
	iJobId(null, "\"0\""), //角色职业，没有职业，填0
	dtEventTime(null, "timeService.now()"), //当前时间
	
	/**--------------------- 从Player对象获取属性 -------------------------*/
	iUin_acct(Player.class, "player.getAccountId()", Player.class.getName()), //用户openid
	dtCreateTime_acct(null, "\"0\""), //账号创建时间，没有账号表，填0
	dtLoginTime_acct(Player.class, "player.getLoginTime()"), //账号登陆时间
	vClientIp_acct(Player.class, "player.getClientIp()"), //角色登录时客户端所在ip
	iLoginWay_acct(Player.class, "player.getChannel()"), //注册渠道
	vDeviceMac_acct(Player.class, "player.getDeviceMac()"), //设备Mac地址（手游）
	vDeviceId_acct(Player.class, "player.getDeviceId()"), //设备唯一标示ID（手游）
	vDeviceInfo_acct(Player.class, "player.getDeviceInfo()"), //设备型号信息（手游）
	vDeviceOS_acct(Player.class, "player.getDeviceOS()"), //设备操作系统信息（手游）
	//本次登录在线时间
	iOnlineTime_acct(Player.class, "TimeUnit.MILLISECONDS.toSeconds((timeService.now()-player.getLoginTime()))", TimeUnit.class.getName()),
	
	/**--------------------- 从Human对象获取属性 -------------------------*/
	iUin(Human.class, "human.getPlayer().getAccountId()"), //用户openid
	vClientIp(Human.class, "human.getPlayer().getClientIp()"), //角色登录时客户端所在ip
	iRoleId(Human.class, "human.getDbId()"), //角色ID
	vRoleName(Human.class, "human.getName()"), //角色名
	iLoginWay(Human.class, "human.getPlayer().getChannel()"), //注册渠道
	
	vDeviceMac(Human.class, "human.getPlayer().getDeviceMac()"), //设备Mac地址（手游）
	vDeviceId(Human.class, "human.getPlayer().getDeviceId()"), //设备唯一标示ID（手游）
	vDeviceInfo(Human.class, "human.getPlayer().getDeviceInfo()"), //设备型号信息（手游）
	vDeviceOS(Human.class, "human.getPlayer().getDeviceOS()"), //设备操作系统信息（手游）
	iClientType(Human.class, "human.getPlayer().getClientType()"), //客户端类型，如1：微端2：手机端3：Pad端
	
	iRoleLevel(Human.class, "human.getInt(HumanPropId.LEVEL)", HumanPropId.class.getName()), //角色等级
	iRoleExp(Human.class, "human.getInt(HumanPropId.EXP)"), //角色等级经验
	iVipLevel(Human.class, "human.getInt(HumanPropId.VIP_LEVEL)"), //VIP等级
	iEnergy(Human.class, "human.getInt(HumanPropId.ENERGY)"), //体力值
	
	iAccumDeposit(Human.class, "human.getAccumulatedChargeDiamond()"), //角色累计充值钻石数
	iAccumConsume(Human.class, "human.getCurrencyManager().get(CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND)", CurrencyPropId.class.getName()), //角色累计消耗钻石数
	iMoney(Human.class, "human.getCurrencyManager().get(CurrencyPropId.GOLD)"), //角色身上的金币数
	iGamePoints(Human.class, "human.getCurrencyManager().get(CurrencyPropId.CHARGE_DIAMOND)"), //角色登入时拥有钻石数量
	
	dtCreateTime(Human.class, "human.getCreateTime()"), //角色创建的时间
	dtLoginTime(Human.class, "human.getLoginTime()"), //登入游戏的时间
	iTotalOnlineTime(Human.class, "human.getTotalOnlineTime()"), //角色总共在线时长(从玩游戏开始算)
	iOnlineTotalTime(Human.class, "human.getTotalOnlineTime()"), //等于iTotalOnlineTime
	//本次登录在线时间
	iOnlineTime(Human.class, "TimeUnit.MILLISECONDS.toSeconds((timeService.now()-human.getLoginTime().getTime()))"),
	
	;
	
	/** 从什么类型中取得该字段的值 */
	public final Class<?> fromType;
	/** 怎么取，注意：来源变量的命名规范是把类名的第一个字符小写（举个例子：Human.class的来源变量名是human） */
	public final String howToGet;
	/** 导入的类，在获取值得时候可能需要导入一些特定的类型 */
	public final Set<String> importClasses = Sets.newHashSet();
	
	SpecialArg(Class<?> fromType, String howToGet, String... importClasses) {
		this.fromType = fromType;
		this.howToGet = howToGet;
		for(String importClass : importClasses) {
			this.importClasses.add(importClass);
		}
	}
	
	private static Map<String, SpecialArg> values = Maps.newHashMap();
	static {
		for(SpecialArg specialArg : SpecialArg.values()) {
			values.put(specialArg.name(), specialArg);
		}
	}
	
	public static SpecialArg get(String name) {
		return values.get(name);
	}
}
