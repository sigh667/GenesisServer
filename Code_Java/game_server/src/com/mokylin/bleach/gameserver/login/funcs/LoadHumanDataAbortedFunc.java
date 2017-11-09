package com.mokylin.bleach.gameserver.login.funcs;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.login.protocol.LoadHumanDataAborted;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 放弃加载角色的函数对象。<p>
 * 
 * 该函数对象在PlayerManagerActor中执行。
 * 
 * @author pangchong
 *
 */
public class LoadHumanDataAbortedFunc implements IServerMsgFunc<LoadHumanDataAborted, ServerGlobals, PlayerManagerArgs> {

	@Override
	public void handle(IRemote remote, LoadHumanDataAborted msg, ServerGlobals sGlobals, PlayerManagerArgs playerManagerArgs) {
		playerManagerArgs.onlinePlayerService.removePlayer(msg.player.getChannel(), msg.player.getAccountId());
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.PLAYER_MANAGER;
	}

}
