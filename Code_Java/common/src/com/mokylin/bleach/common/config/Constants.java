package com.mokylin.bleach.common.config;

import java.util.TimeZone;

import scala.collection.mutable.StringBuilder;

/**
 * 游戏逻辑中的所有由策划指定的一维常量<p>
 * 支持所有基本数据类型
 * @author baoliang.shen
 *
 */
public class Constants {
	
	public static final String fileName = "constants.xml";
	
	/** 当前服务器运行的时区，默认为操作系统时区 */
	private String timeZoneID = TimeZone.getDefault().getID();
	/**体力恢复间隔，单位：秒*/
	private int energyRecoverSecond = 60;
	/**体力恢复值，点/每次 */
	private int energyRecoverValue = 1;
	/**体力上限*/
	private int maxEnergy = 500;
	/**玩家每次购买得到的体力值*/
	private int energyBuyingValue = 100;
	/**玩家等级上限*/
	private int maxLevel = 100;
	/**运营日志-记录在线日志的时间间隔，单位：秒*/
	private int logOnlineCountsPeriod = 300;


	public String getTimeZoneID() {
		return timeZoneID;
	}
	public int getEnergyRecoverSecond() {
		return energyRecoverSecond;
	}
	public int getEnergyRecoverValue() {
		return energyRecoverValue;
	}
	public int getMaxEnergy() {
		return maxEnergy;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public int getEnergyBuyingValue() {
		return energyBuyingValue;
	}
	public int getLogOnlineCountsPeriod() {
		return logOnlineCountsPeriod;
	}
	
	/**
	 * 校验数据合法性
	 */
	public void validate() {
		StringBuilder errMsg = new StringBuilder();
		String lineBreak = System.getProperty("line.separator");
		
		// 校验配置项, 记录错误日志
		if (energyRecoverSecond <= 0) {
			errMsg.append("体力恢复间隔（energyRecoverSecond）必须>0").append(lineBreak);
		}
		if (energyRecoverValue <= 0) {
			errMsg.append("体力恢复值（energyRecoverValue）必须>0").append(lineBreak);
		}
		if (energyBuyingValue <= 0) {
			errMsg.append("玩家每次购买得到的体力值（energyBuyingValue）必须>0").append(lineBreak);
		}
		if (maxEnergy <= 0) {
			errMsg.append("体力上限（maxEnergy）必须>0").append(lineBreak);
		}
		if (maxLevel <= 0) {
			errMsg.append("玩家等级上限（maxLevel）必须>0").append(lineBreak);
		}
		if (logOnlineCountsPeriod <= 0) {
			errMsg.append("记录在线日志的时间间隔（logOnlineCountsPeriod）必须>0").append(lineBreak);
		}
		
		// 检测到错误则抛出异常
		if (!errMsg.isEmpty()) {
			throw new RuntimeException(errMsg.toString());
		}
	}
}
