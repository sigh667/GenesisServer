package com.genesis.gameserver.login.funcs;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.gameserver.player.Player;
import com.genesis.gameserver.player.PlayerManagerArgs;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.gameserver.PlayerConnected;

/**
 * 处理AgentServer发来的PlayerConnneted消息的函数对象。该消息在玩家上线连接后触发。<p>
 *
 * 该函数对象在PlayerManagerActor中执行。
 *
 * @author pangchong
 *
 */

public class PlayerConnectedFunc
        implements IServerMsgFunc<PlayerConnected, ServerGlobals, PlayerManagerArgs> {

    @Override
    public void handle(IRemote remote, PlayerConnected msg, ServerGlobals sGlobals,
            PlayerManagerArgs playerManagerArgs) {
        sGlobals.addSession(msg.id,
                new Player(remote, msg.id, msg.clientIp, msg.deviceMac, msg.deviceId,
                        msg.deviceInfo, msg.deviceOS, msg.clientType));
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.PLAYER_MANAGER;
    }

}
