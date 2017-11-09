package com.mokylin.bleach.agentserver.core.global;

import com.mokylin.bleach.agentserver.config.AgentServerConfig;
import com.mokylin.bleach.agentserver.core.frontend.gameserver.GameServerFrontManager;
import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.isc.ISCService;
import com.mokylin.bleach.core.isc.RemoteActorManager;

public class Globals {
	
	private static AgentServerConfig serverConfig = null;
	
	private static Akka akka = null;

	private static GameServerFrontManager gameServerManager = new GameServerFrontManager();
	
	private static ISCService iscService = null;
	
	private static RemoteActorManager remoteActorManager = null;
	
	public static void init(){
		serverConfig = AgentServerConfig.getAgentServerConfig();
		akka = new Akka(serverConfig.serverConfig.akkaConfig);
		remoteActorManager = new RemoteActorManager(akka);
		iscService = new ISCService(remoteActorManager, serverConfig.serverConfig);
	}
	
	public static AgentServerConfig getServerConfig(){
		return serverConfig;
	}
	
	public static GameServerFrontManager getGameServerManager(){
		return gameServerManager;
	}

	public static Akka getAkka() {
		return akka;
	}
	
	public static ISCService getISCService(){
		return iscService;
	}

	public static RemoteActorManager getRemoteActorManager() {
		return remoteActorManager;
	}
}
