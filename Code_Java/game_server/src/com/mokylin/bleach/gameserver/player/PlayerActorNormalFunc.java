package com.mokylin.bleach.gameserver.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.japi.Procedure;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.isc.msg.ServerMessage;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.net.msg.CSSMessage;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.heartbeat.PlayerHeartbeat;
import com.mokylin.bleach.gameserver.core.persistance.DataUpdater;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * PlayerActor中玩家登录后实际处理游戏逻辑的函数对象。<p>
 * 
 * PlayerActor的状态分为两种，第一种是创建Human的过程，第二种是玩家登录
 * 成功后处理游戏逻辑的过程，该类就是第二种，第一种在PlayerActor的
 * OnReceived方法中实现。<br>
 * 
 * @author pangchong
 *
 */
public class PlayerActorNormalFunc implements Procedure<Object> {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final PlayerActor playerActor;
	private final ServerGlobals sGlobals;
	private final Player player;
	private final Human human;
	private final DataUpdater dataUpdater;
	
	public PlayerActorNormalFunc(PlayerActor playerActor, Human human, Player player, ServerGlobals sGlobals, DataUpdater dataUpdater){
		this.playerActor = playerActor;
		this.sGlobals = sGlobals;
		this.player = player;
		this.dataUpdater = dataUpdater;
		this.human = human;
	}

	@Override
	public void apply(Object msg) throws Exception {
		try{
			if(msg instanceof ServerMessage){
				handleServerMessage((ServerMessage) msg);
			}else if(msg instanceof PlayerHeartbeat){
				this.human.getTimeAxis().heartbeat();
				dataUpdater.heartbeat();
				((PlayerHeartbeat)msg).done();
			}else{
				playerActor.unhandled(msg);
			}
		}catch(Throwable t){
			log.error("Player error occured.", t);
			playerActor.getContext().become(PlayerActorAbnormalFunc.INSTANCE);
			player.disconnect();
		}		
	}
	
	private void handleServerMessage(ServerMessage sMsg) {
		if(sMsg.msg instanceof CSSMessage){
			handleCSSMsg((CSSMessage)sMsg.msg, player);				
		}else{
			Optional<IRemote> option = sGlobals.getISCService().getRemote(sMsg.sType, sMsg.sId);
			if(option.isPresent()){
				Globals.getServerMsgFuncService().handle(MessageTarget.PLAYER, option.get(), sMsg.msg, human, sGlobals);
			}else{
				log.warn("PlayerManagerActor receive a message [{}] can not find remote: ServerType:[{}], ServerId:[{}]",
						sMsg.msg.getClass().getName(), sMsg.sType.name(), sMsg.sId);
			}				
		}
	}
	
	private void handleCSSMsg(CSSMessage msg, Player player) {
		if(player.getStatus().isCanProcess(msg.messageType)){
			Globals.getClientMsgFuncService().handle(MessageTarget.PLAYER, msg.messageType, msg.messageContent, player, human, sGlobals);
		}else{
			log.warn("PlayerManagerActor: Wrong message [{}] is received in Player Status: [{}], Player session id: [{}]", 
					msg.messageType, player.getStatus().name(), player.getId());
		}		
	}

}
