package com.mokylin.bleach.core.isc.msg;

import com.mokylin.bleach.core.net.msg.SCMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 需要转发给玩家客户端的消息
 * 
 * @author yaguang.xiao
 *
 */

public class ToPlayerMessage implements IMessage {
	
	/** 玩家账号Id */
	public final long agentSessionId;
	/** 需要转发给玩家客户端的消息 */
	public final SCMessage msg;

	public ToPlayerMessage(long agentSessionId, SCMessage msg) {
		this.agentSessionId = agentSessionId;
		this.msg = msg;
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}
}
