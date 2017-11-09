package com.mokylin.bleach.gameserver.login.funcs;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.login.protocol.LoadHumanDataSuccess;
import com.mokylin.bleach.gameserver.player.CreatePlayerActorHelper;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 加载角色成功的函数对象。<p>
 * 
 * 该函数对象在PlayerManagerActor中执行。
 * 
 * @author pangchong
 *
 */

public class LoadHumanDataSuccessFunc implements IServerMsgFunc<LoadHumanDataSuccess, ServerGlobals, PlayerManagerArgs> {

	@Override
	public void handle(IRemote remote, LoadHumanDataSuccess msg, ServerGlobals sGlobals, PlayerManagerArgs playerManagerArgs) {
		
		Player player = msg.player;
		if(player.getStatus() == LoginStatus.Logouting){
			playerManagerArgs.onlinePlayerService.removePlayer(player.getChannel(), player.getAccountId());
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
