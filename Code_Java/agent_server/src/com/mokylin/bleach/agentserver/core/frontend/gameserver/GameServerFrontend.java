package com.mokylin.bleach.agentserver.core.frontend.gameserver;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;

import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.core.isc.msg.ServerMessage;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.net.msg.CSSMessage;
import com.mokylin.bleach.protobuf.MessageType;
import com.mokylin.bleach.protobuf.MessageType.CGMessageType;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.td.network2client.core.session.IClientSession;

public class GameServerFrontend{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final IRemote remote;
	private final ServerConfig localConfig;
	/** UUID - PlayerActorRef */
	private ConcurrentHashMap<Long, ActorRef> playerMap = null;
	
	public GameServerFrontend(IRemote remote, ServerConfig localConfig){
		this.localConfig = localConfig;
		this.remote = remote;		
		this.playerMap = new ConcurrentHashMap<>();
	}

	public void route(IClientSession session, CSSMessage msg) {
		CGMessageType msgType = CGMessageType.valueOf(msg.messageType);
		MessageTarget target = msgType.getValueDescriptor().getOptions().getExtension(MessageType.tARGET);
		
		if(isNotLoginMsgAndPlayerNotLogin(msg, target)){
			log.warn("Received a wired message [{}] from player {} before player login", msg.messageType, msg.agentSessionId);
			return;
		}

		if(target == MessageTarget.PLAYER){
			playerMap.get(msg.agentSessionId).tell(new ServerMessage(localConfig.serverType, localConfig.serverId, msg), ActorRef.noSender());
		}else{
			this.remote.sendMessage(msg);
		}			
	}
	
	public void sendMessage(long agentSessionId, IMessage msg){
		if(msg.getTarget() != MessageTarget.PLAYER){
			this.remote.sendMessage(msg);
		}else{
			ActorRef ar = playerMap.get(agentSessionId);
			if(ar != null){
				ar.tell(new ServerMessage(localConfig.serverType, localConfig.serverId, msg), ActorRef.noSender());
			}
		}
	}

	private boolean isNotLoginMsgAndPlayerNotLogin(CSSMessage msg, MessageTarget target) {
		return !isLoginMsg(target) && !playerMap.containsKey(msg.agentSessionId);
	}

	private boolean isLoginMsg(MessageTarget target) {
		return target == MessageTarget.PLAYER_MANAGER;
	}
	
	public void userLogined(long agentSessionId, ActorRef playerActor){
		ActorRef pre = playerMap.putIfAbsent(agentSessionId, playerActor);
		if(pre != null){
			log.warn("User {} has already logined", agentSessionId);
		}
	}
	
	public int getGameServerId(){
		return this.remote.getServerId();
	}

	public void logoutPlayer(long sessionId) {
		playerMap.remove(sessionId);
	}
}
