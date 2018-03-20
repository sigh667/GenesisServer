package com.genesis.servermsg.remotelogserver;

import com.genesis.core.isc.msg.IMessage;
import com.genesis.protobuf.MessageType.MessageTarget;

public class Shutdown implements IMessage {

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
