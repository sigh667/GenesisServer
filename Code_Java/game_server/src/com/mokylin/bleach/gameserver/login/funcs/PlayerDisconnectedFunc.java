package com.mokylin.bleach.gameserver.login.funcs;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.login.log.LoginLogger;
import com.mokylin.bleach.gameserver.player.LoginStatus;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.player.PlayerManagerArgs;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.gameserver.PlayerDisconnected;

/**
 * 
 * 处理AgentServer发来的PlayerDisconnected消息的函数对象。该消息在玩家下线的时候触发。<p>
 * 
 * 该函数对象在PlayerManagerActor中执行。
 * 
 * @author pangchong
 *
 */

public class PlayerDisconnectedFunc implements IServerMsgFunc<PlayerDisconnected, ServerGlobals, PlayerManagerArgs> {

	@Override
	public void handle(IRemote remote, PlayerDisconnected msg, ServerGlobals sGlobals, PlayerManagerArgs playerManager) {
		final long sessionId = msg.agentSessionId;
		Optional<Player> option = sGlobals.removeSession(sessionId);
		if(option.isPresent()){
			Player player = option.get();
			if(player.getStatus() == LoginStatus.Gaming){
				player.getStatus().logoutInPlayerManagerActor(player, playerManager);
				sGlobals.getActorGlobals().letPlayerStop(player.getUuid());
			}else{
				player.getStatus().logoutInPlayerManagerActor(player, playerManager);
			}			
		}else{
			LoginLogger.log.warn("The Session of Agent Session Id: {} does not exist.", sessionId);
		}
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.PLAYER_MANAGER;
	}
}
