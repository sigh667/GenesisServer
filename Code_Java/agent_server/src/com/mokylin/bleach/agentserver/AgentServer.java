package com.mokylin.bleach.agentserver;

import io.netty.channel.ChannelHandler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.google.common.base.Optional;
import com.mokylin.bleach.agentserver.channel.AgentServerChannelListener;
import com.mokylin.bleach.agentserver.config.AgentServerConfig;
import com.mokylin.bleach.agentserver.core.frontend.gameserver.GameServerRemoteFactory;
import com.mokylin.bleach.agentserver.core.global.Globals;
import com.mokylin.bleach.agentserver.core.net.codec.ClientToAgentMessageDecoder;
import com.mokylin.bleach.agentserver.handlers.AgentClientMessageHandler;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.config.model.NetInfo;
import com.mokylin.bleach.core.isc.ISCActorSupervisor;
import com.mokylin.bleach.core.isc.RemoteActorManager;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.msg.RegisterToRemote;
import com.mokylin.bleach.core.isc.msg.RegisterToRemoteResult;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.remote.RemoteServerConfig;
import com.mokylin.bleach.core.net.NettyNetworkLayer;
import com.mokylin.bleach.servermsg.loginserver.PlayerLogout;
import com.mokylin.td.network2client.core.channel.ChannelInitializerImpl;
import com.mokylin.td.network2client.core.handle.ServerIoHandler;

public class AgentServer {
	
	private static Logger log = LoggerFactory.getLogger(AgentServer.class);
	
	public static void main(String[] args) {
		
		Globals.init();
		
		AgentServerConfig config = Globals.getServerConfig();
		ActorRef actorLocal = Globals.getAkka().getActorSystem().actorOf(Props.create(ISCActorSupervisor.class, config.serverConfig, 
				Globals.getISCService(), "com.mokylin.bleach.agentserver", new GameServerRemoteFactory()), ISCActorSupervisor.ACTOR_NAME);
		
		// 启动用于监听MapServer消息的Netty
		try {
			NetInfo netInfoToMapServer = config.netInfoToMapServer;
			NettyNetworkLayer.configNet(netInfoToMapServer.getHost(), netInfoToMapServer.getPort());
//			.addMessageProcess(mp, rs)
//			.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-2);
		}
		
		// 开始尝试连接LoginServer
		while (!connectToLoginServer()) {
			try {
				final int sleepMS = 1000;
				Thread.sleep(sleepMS);
			} catch (InterruptedException e) {
				log.error("Connecting To LoginServer.Thread.sleep()", e);
			}
		}
		// 注册Actor到LoginServer
		ServerConfig loginConfig = config.loginServerConfig;
		if(!registerToRemote(loginConfig.serverType, loginConfig.serverId, actorLocal)) {
			//注册失败
			System.exit(-1);
		}
		// 测试发消息
		Optional<IRemote> remote = Globals.getISCService().getRemote(loginConfig.serverType, loginConfig.serverId);
		remote.get().sendMessage(new PlayerLogout("2","B"));

		// 一切都准备好了之后,启动用于监听客户端消息的Netty
		try {
			NetInfo netInfoToClient = config.netInfoToClient;
			AgentClientMessageHandler mp = new AgentClientMessageHandler();
			AgentServerChannelListener rs = new AgentServerChannelListener();
			ServerIoHandler handler = new ServerIoHandler(mp, rs);
			ChannelHandler childHandler = new ChannelInitializerImpl(handler, ClientToAgentMessageDecoder.class);
			
			NettyNetworkLayer.configNet(netInfoToClient.getHost(), netInfoToClient.getPort())
			.start(childHandler);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**
	 * 尝试连接LoginServer
	 * @return 是否连接成功
	 */
	private static boolean connectToLoginServer() {
		AgentServerConfig config = Globals.getServerConfig();
		ServerConfig loginConfig = config.loginServerConfig;
		RemoteActorManager remoteActorManager = Globals.getRemoteActorManager();
		if(!remoteActorManager.connectRemote(new RemoteServerConfig(loginConfig.serverType, loginConfig.serverId, loginConfig.akkaConfig.ip, loginConfig.akkaConfig.port), 
				ISCActorSupervisor.ACTOR_NAME)){
			log.info("Connect to LoginServer failed! id: [{}], ip: [{}], port: [{}]", loginConfig.serverId, loginConfig.akkaConfig.ip, loginConfig.akkaConfig.port);
			return false;
		}
		return true;
	}
	
	private static boolean registerToRemote(ServerType serverType, int sId, ActorRef actorRef){
		Timeout timeout = new Timeout(Duration.create(5000, TimeUnit.SECONDS));
		try {
			Future<Object> future = Patterns.ask(actorRef, new RegisterToRemote(serverType, sId), timeout);
			RegisterToRemoteResult result = (RegisterToRemoteResult)Await.result(future, timeout.duration());
			if(result == null || !result.isSuccess) throw new RuntimeException("AgentServer register to LoginServer Failed!");
			return true;
		} catch (Exception e) {
			log.error("AgentServer register to LoginServer arise exception:", e);
			return false;
		}
	}

}
