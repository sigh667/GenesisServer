package com.genesis.gameserver.login.funcs;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.player.LoginStatus;
import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.gameserver.login.protocol.LoadHumanDataFailed;
import com.genesis.gameserver.player.Player;
import com.genesis.gameserver.player.PlayerManagerArgs;
import com.genesis.protobuf.MessageType.MessageTarget;

/**
 * 处理加载角色错误的函数对象。<p>
 *
 * 该函数对象在PlayerManagerActor中执行。
 *
 * @author pangchong
 *
 */
public class LoadHumanDataFailedFunc
        implements IServerMsgFunc<LoadHumanDataFailed, ServerGlobals, PlayerManagerArgs> {

    @Override
    public void handle(IRemote remote, LoadHumanDataFailed msg, ServerGlobals sGlobals,
            PlayerManagerArgs playerManagerArgs) {
        Player player = msg.player;
        if (player.getStatus() != LoginStatus.Logouting) {
            player.setStatus(LoginStatus.LoadingHumanFailed);
            //            player.sendMessage(SCLoginFail.newBuilder()
            //                    .setFailReason(LoginFailReason.LOAD_ROLE_FAIL));
            player.disconnect();
        } else {
            playerManagerArgs.onlinePlayerService
                    .removePlayer(player.getChannel(), player.getAccountId());
        }
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.PLAYER_MANAGER;
    }

}
