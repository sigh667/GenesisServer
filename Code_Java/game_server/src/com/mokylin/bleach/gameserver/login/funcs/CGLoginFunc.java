package com.mokylin.bleach.gameserver.login.funcs;

import akka.actor.ActorRef;

import com.google.common.base.Optional;
import com.mokylin.bleach.gameserver.core.concurrent.ArgsRunnable;
import com.mokylin.bleach.gameserver.core.concurrent.AsyncArgs;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.login.LoginService;
import com.mokylin.bleach.gameserver.login.protocol.PlatformAuthResult;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.protobuf.PlayerMessage.CGLogin;

/**
 * 处理客户端登录消息的函数对象。<p>
 * 
 * 该函数对象在PlayerManagerActor中执行。
 * 
 * @author pangchong
 *
 */
public class CGLoginFunc extends AbstractClientMsgFunc<CGLogin, ServerGlobals, PlayerManagerArgs> {

	@Override
	public void handle(Player player, CGLogin msg, ServerGlobals globals, PlayerManagerArgs playerManagerArgs) {
		
		final String channel = msg.getChannel();
		final String accountId = msg.getAccountId();
		final String key = msg.getKey();
		
		LoginService loginService = playerManagerArgs.loginService;
		Optional<Player> option = loginService.getLoginPlayer(channel, accountId);
		if(!option.isPresent()){
			//没有相同账号的正在登录，直接去平台验证
			player.setStatus(LoginStatus.Authing);
			loginService.addLoginPlayer(channel, accountId, player);
			gotoLocalAuth(channel, accountId, key, player, globals.isLocalLoginAllowed(), playerManagerArgs.context.self());
			return;
		}
		
		//存在相同账号正在登录，则全部踢掉
		player.disconnect();
		option.get().disconnect();
	}

	private void gotoLocalAuth(final String channel, final String accountId, final String key, final Player player, boolean isLocalLoginAllowed, final ActorRef playerManagerActorRef) {
		if(!isLocalLoginAllowed || !key.isEmpty()){
			//去往平台验证
			Globals.getPlatformAuthProcessUnit().submitTask(new ArgsRunnable() {
				@Override
				public void run(AsyncArgs args) {
					// 验证账号
					// 将结果发送回PlayerManagerActor
					playerManagerActorRef.tell(new PlatformAuthResult(true, accountId, channel, key, player), ActorRef.noSender());
				}
			});
		}else{
			playerManagerActorRef.tell(new PlatformAuthResult(true, accountId, channel, key, player), ActorRef.noSender());
		}
		
	}

}
