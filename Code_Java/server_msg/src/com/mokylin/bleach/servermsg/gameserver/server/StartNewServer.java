package com.mokylin.bleach.servermsg.gameserver.server;

import akka.actor.ActorRef;

import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class StartNewServer implements IMessage{
	
	public final ServerConfig newServerConfig;
	
	public StartNewServer(ServerConfig config){
		this.newServerConfig = config;
	}
	
	public static class Failed{
		public final String msg;
		public Failed(String msg){
			this.msg = msg;
		}
	}
	
	public static class Succeed{
		public final ActorRef newServer;
		public Succeed(ActorRef newServer){
			this.newServer = newServer;
		}
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.SERVER_MANAGER;
	}
}
