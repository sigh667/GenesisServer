package com.mokylin.bleach.gameserver;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;

import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.ISCActorSupervisor;
import com.mokylin.bleach.core.isc.remote.RemoteServerConfig;
import com.mokylin.bleach.gameserver.core.config.GameServerConfig;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.server.ServerManagerActor;

public class GameServer {
	
	private static Logger log = LoggerFactory.getLogger(GameServer.class);

	public static void main(String[] args) {
		try{
			// 加载配置文件并初始化本物理服务器全局数据
			Globals.init();
			
			connectToAgentServer();
			
			connectToDataServer();
			
			// 启动Akka并创建本物理服务器的逻辑服务器管理器
			Akka akka = Globals.getAkka();
			akka.getActorSystem().actorOf(Props.create(ServerManagerActor.class, GameServerConfig.getGameServerConfig()), ServerManagerActor.ACTOR_NAME);
			
			log.info("GameServer start OK！----------------------------------------------------");
			
		}catch(Throwable t){
			t.printStackTrace();
			System.exit(-1);
		}
		
	}

	private static void connectToDataServer() {
		List<ServerConfig> dataConfigs = GameServerConfig.getConnectedDataConfig();
		if(dataConfigs == null || dataConfigs.isEmpty()){
			log.error("Game server can not start with no data server connected!");
			shutdown();
		}
		
		for(ServerConfig config : dataConfigs){
			if(!Globals.getRemoteActorManager().connectRemote(new RemoteServerConfig(config.serverType, config.serverId, config.akkaConfig.ip, config.akkaConfig.port), ISCActorSupervisor.ACTOR_NAME)){
				log.error("GameServer Start Failed! Can not connect to DataServer id: [{}], ip: [{}], port: [{}]", config.serverId, config.akkaConfig.ip, config.akkaConfig.port);
				shutdown();
			}
		}
	}

	private static void connectToAgentServer() {
		ServerConfig agentConfig = GameServerConfig.getConnectedAgentConfig();
		if(!Globals.getRemoteActorManager().connectRemote(new RemoteServerConfig(agentConfig.serverType, agentConfig.serverId, agentConfig.akkaConfig.ip, agentConfig.akkaConfig.port), 
				ISCActorSupervisor.ACTOR_NAME)){
			log.error("GameServer Start Failed! Can not connect to AgentServer id: [{}], ip: [{}], port: [{}]", agentConfig.serverId, agentConfig.akkaConfig.ip, agentConfig.akkaConfig.port);
			shutdown();
		}
	}
	
	private static void shutdown() {
		Globals.getAkka().getActorSystem().shutdown();
		System.exit(-1);
	}

}
