package com.mokylin.bleach.gameserver.player;

import com.mokylin.bleach.core.annotation.NotThreadSafe;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.session.PlayerSession;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.servermsg.agentserver.RemovePlayerMsg;

/**
 * 玩家的Session对象。<p>
 * 
 * 该类的实例方法中，set类的方法只能在PlayerManagerActor中调用，其它方法在任意地方均可使用。
 * 
 * @author pangchong
 *
 */
@NotThreadSafe
public class Player extends PlayerSession {
	
	/** 账号ID */
	private volatile String accountId = "";
	/** 渠道 */
	private volatile String channel = "";
	/** 登录用的平台秘钥 */
	private volatile String key = "";
	/** 玩家登录后所用角色的UUID(同一个账号只能一个角色登录) */
	private volatile long uuid = -1;
	/** 玩家当前游戏的状态 */
	private volatile LoginStatus status = LoginStatus.Init;
	/** 登陆时间*/
	private volatile long loginTime;
	
	/** 客户端地址 */
	private final String clientIp;
	/** 设备Mac地址 */
	private final String deviceMac;
	/** 设备唯一标识ID */
	private final String deviceId;
	/** 设备型号信息 */
	private final String deviceInfo;
	/** 设备操作系统信息 */
	private final String deviceOS;
	/** 客户端类型 */
	private final String clientType;

	public Player(IRemote remote, long agentSessionId, String clientIp, String deviceMac, String deviceId, String deviceInfo, String deviceOS, String clientType) {
		super(remote, agentSessionId);
		this.clientIp = clientIp;
		this.deviceMac = deviceMac;
		this.deviceId = deviceId;
		this.deviceInfo = deviceInfo;
		this.deviceOS = deviceOS;
		this.clientType = clientType;
	}

	/**
	 * 用于初始化账号登陆时间。
	 * <br>* 非线程安全，仅在PlayerManagerActor中调用
	 */
	public void initLoginTime(){
		this.loginTime = Globals.getTimeService().now();
	}
	
	public LoginStatus getStatus() {
		return status;
	}

	public void setStatus(LoginStatus status) {
		this.status = status;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getChannel() {
		return channel;
	}
	
	public void setKey(String key){
		this.key  = key;
	}
	
	public String getKey() {
		return this.key;
	}

	/**
	 * 让玩家下线。
	 */
	public void disconnect() {
		if (this.getStatus()==LoginStatus.Logouting) {
			//已经正在退出了
			return;
		}
		//通知网关，断开此Player的网络链接
		this.sendMessage(new RemovePlayerMsg(this.getId()));
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public boolean isGmAccount() {
		return true;
	}

	public String getClientIp() {
		return clientIp;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public String getClientType() {
		return clientType;
	}

	public long getLoginTime() {
		return loginTime;
	}
	
}
