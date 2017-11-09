package com.mokylin.bleach.servermsg.logserver;

import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 停服消息
 * 
 * @author yaguang.xiao
 *
 */

public class Shutdown implements IMessage{

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}

}
