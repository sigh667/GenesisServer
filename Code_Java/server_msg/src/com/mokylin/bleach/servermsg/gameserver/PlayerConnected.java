package com.mokylin.bleach.servermsg.gameserver;

import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;


/**
 * 网关通知GameServer，创建Player对象，准备接受玩家登陆
 * @author baoliang.shen
 *
 */
public class PlayerConnected implements IMessage{
	public final long id;
	public final String clientIp;
	/** 设备Mac地址 */
	public final String deviceMac;
	/** 设备唯一标识ID */
	public final String deviceId;
	/** 设备型号信息 */
	public final String deviceInfo;
	/** 设备操作系统信息 */
	public final String deviceOS;
	/** 客户端类型 */
	public final String clientType;
	
	public PlayerConnected(long agentSessionId, String clientIp, String deviceMac, String deviceId, String deviceInfo, String deviceOS, String clientType){
		this.id = agentSessionId;
		this.clientIp = clientIp;
		this.deviceMac = deviceMac;
		this.deviceId = deviceId;
		this.deviceInfo = deviceInfo;
		this.deviceOS = deviceOS;
		this.clientType = clientType;
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.PLAYER_MANAGER;
	}
}
