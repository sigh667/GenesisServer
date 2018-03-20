package com.genesis.gameserver.login.funcs;

import com.google.common.base.Optional;

import com.genesis.gameserver.core.concurrent.ArgsRunnable;
import com.genesis.gameserver.core.concurrent.AsyncArgs;
import com.genesis.gameserver.core.global.Globals;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.login.LoginService;
import com.genesis.gameserver.login.protocol.PlatformAuthResult;
import com.genesis.gameserver.player.LoginStatus;
import com.genesis.gameserver.player.Player;
import com.genesis.gameserver.player.PlayerManagerArgs;
import com.genesis.protobuf.LoginMessage.CSLogin;

import akka.actor.ActorRef;

/**
 * 处理客户端登录消息的函数对象。<p>
 *
 * 该函数对象在PlayerManagerActor中执行。
 *
 * @author pangchong
 *
 */
public class CGLoginFunc extends AbstractClientMsgFunc<CSLogin, ServerGlobals, PlayerManagerArgs> {

    @Override
    public void handle(Player player, CSLogin msg, ServerGlobals globals,
            PlayerManagerArgs playerManagerArgs) {

        final String channel = msg.getChannel();
        final String accountId = msg.getAccountId();
        final String key = msg.getKey();

        LoginService loginService = playerManagerArgs.loginService;
        Optional<Player> option = loginService.getLoginPlayer(channel, accountId);
        if (!option.isPresent()) {
            //没有相同账号的正在登录，直接去平台验证
            player.setStatus(LoginStatus.Authing);
            loginService.addLoginPlayer(channel, accountId, player);
            gotoLocalAuth(channel, accountId, key, player, globals.isLocalLoginAllowed(),
                    playerManagerArgs.context.self());
            return;
        }

        //存在相同账号正在登录，则全部踢掉
        player.disconnect();
        option.get().disconnect();
    }

    private void gotoLocalAuth(final String channel, final String accountId, final String key,
            final Player player, boolean isLocalLoginAllowed,
            final ActorRef playerManagerActorRef) {
        if (!isLocalLoginAllowed || !key.isEmpty()) {
            //去往平台验证
            Globals.getPlatformAuthProcessUnit().submitTask(new ArgsRunnable() {
                @Override
                public void run(AsyncArgs args) {
                    // 验证账号
                    // 将结果发送回PlayerManagerActor
                    playerManagerActorRef
                            .tell(new PlatformAuthResult(true, accountId, channel, key, player),
                                    ActorRef.noSender());
                }
            });
        } else {
            playerManagerActorRef
                    .tell(new PlatformAuthResult(true, accountId, channel, key, player),
                            ActorRef.noSender());
        }

    }

}
