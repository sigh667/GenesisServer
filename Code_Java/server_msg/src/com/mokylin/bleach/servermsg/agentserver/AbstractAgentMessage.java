package com.mokylin.bleach.servermsg.agentserver;

import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public abstract class AbstractAgentMessage implements IMessage {

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}

}
