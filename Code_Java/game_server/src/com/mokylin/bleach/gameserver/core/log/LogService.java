package com.mokylin.bleach.gameserver.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.time.TimeService;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.log.disruptor.sendlog.ISendLog;
import java.util.concurrent.TimeUnit;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.gameserver.player.Player;

/**
 * This is an auto generated source,please don't modify it.
 */
public class LogService {

	private static final Logger logger = LoggerFactory
			.getLogger(LogService.class);
	private final TimeService timeService = Globals.getTimeService();
	private final ServerGlobals serverGlobals;
	private final ISendLog sendLog;
	private final String logStrPrefix;

	public LogService(ServerGlobals serverGlobals, ISendLog sendLog) {
		this.serverGlobals = serverGlobals;
		this.sendLog = sendLog;
		this.logStrPrefix = "_" + serverGlobals.getServerId() + "-";
	}

	/**
	 * 标识在线日志 日志类型标识，GR3评审必要的日志，保留至少31天
	 * 
	 * @param iEventId
	 *            游戏事件ID 不同的游戏事件产生的日志，它们的事件ID也不一样；相同的游戏事件产生的多条日志，它们的游戏事件ID相同。
	 * @param iAccountCount
	 *            记录时间时刻的账号在线数量
	 * @param iAccountMax
	 *            当前记录时间间隔的最大账号在线数量 以每5分钟为间隔
	 * @param iPlayerCount
	 *            记录时间时刻的角色在线数量
	 * @param iPlayerMax
	 *            当前记录时间间隔的最大角色在线数量 以每5分钟为间隔
	 * @param iDeviceCount
	 *            记录时间时刻的设备在线数量（手游）
	 * @param iDeviceMax
	 *            当前记录时间间隔的最大设备在线数量（手游） 以每5分钟为间隔
	 */
	public void logOnlineCount(String iEventId, int iAccountCount,
			int iAccountMax, int iPlayerCount, int iPlayerMax,
			int iDeviceCount, int iDeviceMax) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("0").append(this.logStrPrefix);
			sb.append("OnlineCount").append("|").append(iEventId).append("|")
					.append(timeService.now()).append("|")
					.append(serverGlobals.getServerId()).append("|")
					.append(iAccountCount).append("|").append(iAccountMax)
					.append("|").append(iPlayerCount).append("|")
					.append(iPlayerMax).append("|").append(iDeviceCount)
					.append("|").append(iDeviceMax);
			this.sendLog.sendLog(sb.toString());
		} catch (Exception e) {
			logger.error("日志logOnlineCount记录错误：", e);
		}
	}

	/**
	 * 标识角色登入日志 日志类型标识，GR3评审必要的日志，保留至少60天
	 * 
	 * @param iEventId
	 *            游戏事件ID 不同的游戏事件产生的日志，它们的事件ID也不一样；相同的游戏事件产生的多条日志，它们的游戏事件ID相同。
	 */
	public void logRoleLogin(Human human, String iEventId) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(human.getPlayer().getChannel()).append(this.logStrPrefix);
			sb.append("RoleLogin")
					.append("|")
					.append(iEventId)
					.append("|")
					.append(serverGlobals.getServerId())
					.append("|")
					.append(human.getPlayer().getAccountId())
					.append("|")
					.append(timeService.now())
					.append("|")
					.append(human.getPlayer().getClientIp())
					.append("|")
					.append(human.getDbId())
					.append("|")
					.append(human.getName())
					.append("|")
					.append(human.getInt(HumanPropId.LEVEL))
					.append("|")
					.append(human.getCurrencyManager().get(CurrencyPropId.GOLD))
					.append("|")
					.append(human.getCurrencyManager().get(
							CurrencyPropId.CHARGE_DIAMOND))
					.append("|")
					.append(human.getCreateTime())
					.append("|")
					.append(human.getPlayer().getChannel())
					.append("|")
					.append(human.getPlayer().getDeviceMac())
					.append("|")
					.append(human.getPlayer().getDeviceId())
					.append("|")
					.append(human.getInt(HumanPropId.EXP))
					.append("|")
					.append(human.getInt(HumanPropId.ENERGY))
					.append("|")
					.append(human.getInt(HumanPropId.VIP_LEVEL))
					.append("|")
					.append(human.getAccumulatedChargeDiamond())
					.append("|")
					.append(human.getCurrencyManager().get(
							CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND))
					.append("|").append(human.getTotalOnlineTime()).append("|")
					.append(human.getPlayer().getDeviceInfo()).append("|")
					.append(human.getPlayer().getDeviceOS());
			this.sendLog.sendLog(sb.toString());
		} catch (Exception e) {
			logger.error("日志logRoleLogin记录错误：", e);
		}
	}

	/**
	 * 标识角色登出日志 日志类型标识，GR3评审必要的日志，保留至少60天
	 * 
	 * @param iEventId
	 *            游戏事件ID 不同的游戏事件产生的日志，它们的事件ID也不一样；相同的游戏事件产生的多条日志，它们的游戏事件ID相同。
	 */
	public void logRoleLogout(Human human, String iEventId) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(human.getPlayer().getChannel()).append(this.logStrPrefix);
			sb.append("RoleLogout")
					.append("|")
					.append(iEventId)
					.append("|")
					.append(serverGlobals.getServerId())
					.append("|")
					.append(human.getPlayer().getAccountId())
					.append("|")
					.append(timeService.now())
					.append("|")
					.append(human.getLoginTime())
					.append("|")
					.append(human.getPlayer().getClientIp())
					.append("|")
					.append(human.getCreateTime())
					.append("|")
					.append(TimeUnit.MILLISECONDS.toSeconds((timeService.now() - human
							.getLoginTime().getTime())))
					.append("|")
					.append(human.getTotalOnlineTime())
					.append("|")
					.append(human.getDbId())
					.append("|")
					.append(human.getName())
					.append("|")
					.append(human.getInt(HumanPropId.LEVEL))
					.append("|")
					.append(human.getCurrencyManager().get(CurrencyPropId.GOLD))
					.append("|")
					.append(human.getPlayer().getChannel())
					.append("|")
					.append(human.getPlayer().getDeviceMac())
					.append("|")
					.append(human.getPlayer().getDeviceId())
					.append("|")
					.append(human.getInt(HumanPropId.EXP))
					.append("|")
					.append(human.getInt(HumanPropId.ENERGY))
					.append("|")
					.append(human.getInt(HumanPropId.VIP_LEVEL))
					.append("|")
					.append(human.getAccumulatedChargeDiamond())
					.append("|")
					.append(human.getCurrencyManager().get(
							CurrencyPropId.ACCUMULATED_CHARGE_DIAMOND))
					.append("|").append(human.getPlayer().getDeviceInfo())
					.append("|").append(human.getPlayer().getDeviceOS());
			this.sendLog.sendLog(sb.toString());
		} catch (Exception e) {
			logger.error("日志logRoleLogout记录错误：", e);
		}
	}

	/**
	 * 标识角色登入日志 日志类型标识，GR3评审必要的日志，保留至少60天
	 * 
	 * @param iEventId
	 *            游戏事件ID 不同的游戏事件产生的日志，它们的事件ID也不一样；相同的游戏事件产生的多条日志，它们的游戏事件ID相同。
	 */
	public void logAccountLogin(Player player, String iEventId) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("0").append(this.logStrPrefix);
			sb.append("AccountLogin").append("|").append(iEventId).append("|")
					.append(serverGlobals.getServerId()).append("|")
					.append(player.getAccountId()).append("|")
					.append(timeService.now()).append("|")
					.append(player.getClientIp()).append("|").append("0")
					.append("|").append(player.getChannel()).append("|")
					.append(player.getDeviceMac()).append("|")
					.append(player.getDeviceId()).append("|")
					.append(player.getDeviceInfo()).append("|")
					.append(player.getDeviceOS());
			this.sendLog.sendLog(sb.toString());
		} catch (Exception e) {
			logger.error("日志logAccountLogin记录错误：", e);
		}
	}

	/**
	 * 标识角色登出日志 日志类型标识，GR3评审必要的日志，保留至少60天
	 * 
	 * @param iEventId
	 *            游戏事件ID 不同的游戏事件产生的日志，它们的事件ID也不一样；相同的游戏事件产生的多条日志，它们的游戏事件ID相同。
	 */
	public void logAccountLogout(Player player, String iEventId) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("0").append(this.logStrPrefix);
			sb.append("AccountLogout")
					.append("|")
					.append(iEventId)
					.append("|")
					.append(serverGlobals.getServerId())
					.append("|")
					.append(player.getAccountId())
					.append("|")
					.append(timeService.now())
					.append("|")
					.append(player.getLoginTime())
					.append("|")
					.append(player.getClientIp())
					.append("|")
					.append("0")
					.append("|")
					.append(TimeUnit.MILLISECONDS.toSeconds((timeService.now() - player
							.getLoginTime()))).append("|")
					.append(player.getChannel()).append("|")
					.append(player.getDeviceMac()).append("|")
					.append(player.getDeviceId()).append("|")
					.append(player.getDeviceInfo()).append("|")
					.append(player.getDeviceOS());
			this.sendLog.sendLog(sb.toString());
		} catch (Exception e) {
			logger.error("日志logAccountLogout记录错误：", e);
		}
	}

	/**
	 * 标识角色创建日志 日志类型标识，GR3评审必要的日志，保留至少31天
	 * 
	 * @param iEventId
	 *            游戏事件ID 不同的游戏事件产生的日志，它们的事件ID也不一样；相同的游戏事件产生的多条日志，它们的游戏事件ID相同。
	 */
	public void logCreateRole(Human human, String iEventId) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(human.getPlayer().getChannel()).append(this.logStrPrefix);
			sb.append("CreateRole").append("|").append(iEventId).append("|")
					.append(serverGlobals.getServerId()).append("|")
					.append(human.getPlayer().getAccountId()).append("|")
					.append(timeService.now()).append("|")
					.append(human.getPlayer().getClientIp()).append("|")
					.append(human.getDbId()).append("|")
					.append(human.getName()).append("|").append("0")
					.append("|").append(human.getPlayer().getChannel())
					.append("|").append(human.getPlayer().getDeviceMac())
					.append("|").append(human.getPlayer().getDeviceId())
					.append("|").append(human.getPlayer().getDeviceInfo())
					.append("|").append(human.getPlayer().getDeviceOS())
					.append("|").append(human.getPlayer().getClientType());
			this.sendLog.sendLog(sb.toString());
		} catch (Exception e) {
			logger.error("日志logCreateRole记录错误：", e);
		}
	}

	/**
	 * 标识角色升级日志 日志类型标识，GR3评审必要的日志，保留至少31天
	 * 
	 * @param iEventId
	 *            游戏事件ID 不同的游戏事件产生的日志，它们的事件ID也不一样；相同的游戏事件产生的多条日志，它们的游戏事件ID相同。
	 * @param iReason
	 *            升级原因 打怪升级,使用道具
	 * @param iOriginRoleLevel
	 *            升级前的等级
	 */
	public void logRoleLevelUp(Human human, String iEventId, int iReason,
			long iOriginRoleLevel) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("0").append(this.logStrPrefix);
			sb.append("RoleLevelUp").append("|").append(iEventId).append("|")
					.append(serverGlobals.getServerId()).append("|")
					.append(human.getPlayer().getAccountId()).append("|")
					.append(timeService.now()).append("|")
					.append(human.getPlayer().getClientIp()).append("|")
					.append(human.getDbId()).append("|")
					.append(human.getName()).append("|").append("0")
					.append("|").append(human.getInt(HumanPropId.LEVEL))
					.append("|").append(human.getPlayer().getDeviceMac())
					.append("|").append(human.getPlayer().getDeviceId())
					.append("|").append(human.getTotalOnlineTime()).append("|")
					.append(iReason).append("|")
					.append(human.getInt(HumanPropId.EXP)).append("|")
					.append(human.getPlayer().getDeviceInfo()).append("|")
					.append(human.getPlayer().getDeviceOS()).append("|")
					.append(iOriginRoleLevel);
			this.sendLog.sendLog(sb.toString());
		} catch (Exception e) {
			logger.error("日志logRoleLevelUp记录错误：", e);
		}
	}

}