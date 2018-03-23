package com.genesis.gameserver.login.funcs;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.login.protocol.LoadHumanDataAborted;
import com.genesis.gameserver.player.PlayerManagerArgs;
import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;

/**
 * 放弃加载角色的函数对象。<p>
 *
 * 该函数对象在PlayerManagerActor中执行。
 *
 * @author pangchong
 *
 */
public class LoadHumanDataAbortedFunc
        implements IServerMsgFunc<LoadHumanDataAborted, ServerGlobals, PlayerManagerArgs> {

    @Override
    public void handle(IRemote remote, LoadHumanDataAborted msg, ServerGlobals sGlobals,
            PlayerManagerArgs playerManagerArgs) {
        playerManagerArgs.onlinePlayerService
                .removePlayer(msg.player.getChannel(), msg.player.getAccountId());
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.PLAYER_MANAGER;
    }

}
