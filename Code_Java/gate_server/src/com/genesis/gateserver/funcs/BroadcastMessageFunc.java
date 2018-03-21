package com.genesis.gateserver.funcs;

import com.genesis.gateserver.core.session.AgentClientSessions;
import com.genesis.core.isc.msg.BroadcastMessage;
import com.genesis.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.core.msgfunc.server.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;

public class BroadcastMessageFunc implements IServerMsgFunc<BroadcastMessage, MsgArgs, MsgArgs> {

    @Override
    public void handle(IRemote remote, BroadcastMessage msg, MsgArgs arg1, MsgArgs arg2) {
        AgentClientSessions.Inst.broadcastMsgByUuid(msg.uuids, msg.msg);
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
