package com.genesis.gateserver.funcs;

import com.google.common.base.Optional;

import com.genesis.gateserver.core.frontend.gameserver.GameServerFrontend;
import com.genesis.gateserver.global.Globals;
import com.genesis.gateserver.core.session.AgentClientSessions;
import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.agentserver.PlayerLogined;

public class PlayerLoginedFunc implements IServerMsgFunc<PlayerLogined, MsgArgs, MsgArgs> {

    @Override
    public void handle(IRemote remote, PlayerLogined msg, MsgArgs arg1, MsgArgs arg2) {
        Optional<GameServerFrontend> gsOption =
                Globals.getGameServerManager().getGameServerFrontend(msg.gameServerId);
        if (gsOption.isPresent()) {
            GameServerFrontend gsFrontend = gsOption.get();
            gsFrontend.userLogined(msg.agentSessionId, msg.playerActorRef);
            AgentClientSessions.Inst
                    .putUuid(msg.humanUuid, AgentClientSessions.Inst.get(msg.agentSessionId));
            return;
        }
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
