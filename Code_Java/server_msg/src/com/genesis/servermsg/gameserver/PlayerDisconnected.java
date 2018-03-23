package com.genesis.servermsg.gameserver;

import com.genesis.servermsg.core.msg.IMessage;
import com.genesis.protobuf.MessageType.MessageTarget;

public class PlayerDisconnected implements IMessage {

    public final long agentSessionId;

    public PlayerDisconnected(long agentSessionId) {
        this.agentSessionId = agentSessionId;
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.PLAYER_MANAGER;
    }

}
