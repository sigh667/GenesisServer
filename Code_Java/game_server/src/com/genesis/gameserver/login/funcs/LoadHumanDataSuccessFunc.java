package com.genesis.gameserver.login.funcs;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.login.protocol.LoadHumanDataSuccess;
import com.genesis.gameserver.player.CreatePlayerActorHelper;
import com.genesis.gameserver.player.LoginStatus;
import com.genesis.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.server.IServerMsgFunc;
import com.genesis.gameserver.player.Player;
import com.genesis.gameserver.player.PlayerManagerArgs;
import com.genesis.protobuf.MessageType.MessageTarget;

/**
 * 加载角色成功的函数对象。<p>
 *
 * 该函数对象在PlayerManagerActor中执行。
 *
 * @author pangchong
 *
 */

public class LoadHumanDataSuccessFunc
        implements IServerMsgFunc<LoadHumanDataSuccess, ServerGlobals, PlayerManagerArgs> {

    @Override
    public void handle(IRemote remote, LoadHumanDataSuccess msg, ServerGlobals sGlobals,
            PlayerManagerArgs playerManagerArgs) {

        Player player = msg.player;
        if (player.getStatus() == LoginStatus.Logouting) {
            playerManagerArgs.onlinePlayerService
                    .removePlayer(player.getChannel(), player.getAccountId());
            return;
        }
        player.setStatus(LoginStatus.LoadingHumanSucceed);
        CreatePlayerActorHelper
                .createPlayerActor(player, msg.humanData, sGlobals, playerManagerArgs);
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.PLAYER_MANAGER;
    }

}
