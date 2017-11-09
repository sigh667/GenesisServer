package com.mokylin.bleach.gameserver.login.funcs;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.timeout.TimeoutCallbackManager.TimeoutCBWrapper;
import com.mokylin.bleach.gameserver.player.CreatePlayerActorHelper;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.gameserver.HumanDataMsg;

/**
 * 处理DataServer返回的加载角色消息的函数对象。<p>
 * 
 * 该函数对象在PlayerManagerActor中执行。
 * 
 * @author pangchong
 *
 */

public class HumanDataMsgFunc implements IServerMsgFunc<HumanDataMsg, ServerGlobals, PlayerManagerArgs> {

	@Override
	public void handle(IRemote remote, HumanDataMsg msg, ServerGlobals sGlobals, PlayerManagerArgs playerManagerArgs) {
		Optional<TimeoutCBWrapper> cbOption = sGlobals.getTimeoutCBManager().deregister(msg.timeoutId);
		if(!cbOption.isPresent()){
			//已经超时
			return;
		}
		cbOption.get().cancelTimeoutCB();
		final String channel = msg.channel;
		final String accountId = msg.accountId;
		Optional<Player> option = playerManagerArgs.onlinePlayerService.getPlayer(channel, accountId);
		if(!option.isPresent()) return;
		
		Player player = option.get();
		if (player.getStatus()==LoginStatus.Logouting) {
			playerManagerArgs.onlinePlayerService.removePlayer(channel, accountId);
			return;
		}
		player.setStatus(LoginStatus.LoadingHumanSucceed);
		CreatePlayerActorHelper.createPlayerActor(player, msg.humanData, sGlobals, playerManagerArgs);
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.PLAYER_MANAGER;
	}

}
