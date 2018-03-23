package com.genesis.gateserver.funcs;

import com.genesis.gateserver.core.session.AgentClientSessions;
import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.agentserver.RemovePlayerMsg;
import com.genesis.network2client.session.IClientSession;

public class RemovePlayerMsgFunc implements IServerMsgFunc<RemovePlayerMsg, MsgArgs, MsgArgs> {

    @Override
    public void handle(IRemote remote, RemovePlayerMsg msg, MsgArgs arg1, MsgArgs arg2) {
        IClientSession session = AgentClientSessions.Inst.get(msg.agentSessionId);
        if (session != null) {
            session.disconnect();
        }
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
