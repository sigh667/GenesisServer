package com.mokylin.bleach.servermsg.gameserver;

import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class PlayerDisconnected implements IMessage {
	
	public final long agentSessionId;
	
	public PlayerDisconnected(long agentSessionId){
		this.agentSessionId = agentSessionId;
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.PLAYER_MANAGER;
	}

}
