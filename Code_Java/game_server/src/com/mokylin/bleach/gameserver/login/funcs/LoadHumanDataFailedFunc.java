package com.mokylin.bleach.gameserver.login.funcs;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.login.protocol.LoadHumanDataFailed;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.protobuf.LoginMessage.SCLoginFail;
import com.genesis.protobuf.LoginMessage.LoginFailReason;

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
            player.sendMessage(SCLoginFail.newBuilder()
                    .setFailReason(LoginFailReason.LOAD_ROLE_FAIL));
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
