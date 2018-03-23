package com.genesis.servermsg.logserver;

import com.genesis.servermsg.core.msg.IMessage;
import com.genesis.protobuf.MessageType.MessageTarget;

/**
 * 停服消息
 *
 * @author yaguang.xiao
 *
 */

public class Shutdown implements IMessage {

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
