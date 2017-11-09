package com.mokylin.bleach.servermsg.dataserver;

import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class LoadHumanDataMessage implements IMessage {
	
	/** 超时ID */
	public final long timeoutId;
	/**账号ID*/
	public final String accountId;
	/**渠道*/
	public final String channel;
	/**原服务器ID*/
	public final int originalServerId;
	/**角色ID*/
	public final long humanId;
	
	public LoadHumanDataMessage(long timeoutId, String channel, String accountId, long humanId, int originalServerId) {
		this.timeoutId = timeoutId;
		this.accountId = accountId;
		this.channel = channel;
		this.humanId = humanId;
		this.originalServerId = originalServerId;
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}
}
