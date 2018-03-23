package com.genesis.servermsg.core.isc.msg;

import com.genesis.core.net.msg.SCMessage;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.core.msg.IMessage;

/**
 * 广播消息，用于Server向指定的Clients广播消息。
 *
 * @author yaguang.xiao
 *
 */

public class BroadcastMessage implements IMessage {

    /** 指定的玩家的UUIDs */
    public final long[] uuids;
    public final SCMessage msg;

    public BroadcastMessage(long[] uuids, SCMessage msg) {
        this.uuids = uuids;
        this.msg = msg;
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }
}
