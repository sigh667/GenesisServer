package com.mokylin.bleach.servermsg.loginserver;

import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class PlayerLogout implements IMessage {

	/**账号ID*/
	public final String accountId;
	/**渠道*/
	public final String channel;

	public PlayerLogout(String accountId, String channel) {
		this.accountId = accountId;
		this.channel = channel;
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}
}
