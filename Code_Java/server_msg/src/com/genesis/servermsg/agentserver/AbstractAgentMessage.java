package com.genesis.servermsg.agentserver;

import com.genesis.core.isc.msg.IMessage;
import com.genesis.protobuf.MessageType.MessageTarget;

public abstract class AbstractAgentMessage implements IMessage {

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
