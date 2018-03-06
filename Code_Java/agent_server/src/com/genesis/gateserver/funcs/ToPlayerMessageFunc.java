package com.genesis.gateserver.funcs;

import com.genesis.gateserver.core.session.AgentClientSessions;
import com.mokylin.bleach.core.isc.msg.ToPlayerMessage;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;

public class ToPlayerMessageFunc implements IServerMsgFunc<ToPlayerMessage, MsgArgs, MsgArgs> {

    @Override
    public void handle(IRemote remote, ToPlayerMessage msg, MsgArgs arg1, MsgArgs arg2) {
        AgentClientSessions.Inst.sendToClientByAgentSessionId(msg.agentSessionId, msg.msg);
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
