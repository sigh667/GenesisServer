package com.genesis.gameserver.login.funcs;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.player.LoginStatus;
import com.google.common.base.Optional;

import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.gameserver.core.timeout.TimeoutCallbackManager.TimeoutCBWrapper;
import com.genesis.gameserver.player.CreatePlayerActorHelper;
import com.genesis.gameserver.player.Player;
import com.genesis.gameserver.player.PlayerManagerArgs;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.gameserver.HumanDataMsg;

/**
 * 处理DataServer返回的加载角色消息的函数对象。<p>
 *
 * 该函数对象在PlayerManagerActor中执行。
 *
 * @author pangchong
 *
 */

public class HumanDataMsgFunc
        implements IServerMsgFunc<HumanDataMsg, ServerGlobals, PlayerManagerArgs> {

    @Override
    public void handle(IRemote remote, HumanDataMsg msg, ServerGlobals sGlobals,
            PlayerManagerArgs playerManagerArgs) {
        Optional<TimeoutCBWrapper> cbOption =
                sGlobals.getTimeoutCBManager().deregister(msg.timeoutId);
        if (!cbOption.isPresent()) {
            //已经超时
            return;
        }
        cbOption.get().cancelTimeoutCB();
        final String channel = msg.channel;
        final String accountId = msg.accountId;
        Optional<Player> option =
                playerManagerArgs.onlinePlayerService.getPlayer(channel, accountId);
        if (!option.isPresent()) {
            return;
        }

        Player player = option.get();
        if (player.getStatus() == LoginStatus.Logouting) {
            playerManagerArgs.onlinePlayerService.removePlayer(channel, accountId);
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
