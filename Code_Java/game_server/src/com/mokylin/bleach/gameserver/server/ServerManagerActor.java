package com.mokylin.bleach.gameserver.server;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorInitializationException;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;

import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.mokylin.bleach.gameserver.core.config.GameServerConfig;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.gameserver.server.StartNewServer;

/**
 * 用于管理逻辑服务器Actor的Actor。<p>
 * 
 * 该Actor的监管策略为当子Actor初始化失败时，停止子Actor，否则resume。<p>
 * 
 * 因为ServerMangerActor是GameServer最根部的Actor，它直接监管的是每一个逻辑服务器
 * 的Actor，当这些逻辑服务器的Actor发生Exception时（这里指的是Exception以及其子类的
 * 所有Exception），逻辑服务器应该继续处理消息；当除Exception外的其它Throwable
 * 异常发生时，意味着发生了严重的错误，这里将会把因为该Actor上面是user，所以结果一定
 * 是关闭整个ActorSystem。
 * 
 * @author pangchong
 *
 */
@MessageAcception(MessageTarget.SERVER_MANAGER)
public class ServerManagerActor extends UntypedActor {
	
	private final Logger log = LoggerFactory.getLogger(ServerManagerActor.class);
	
	public final static String ACTOR_NAME = "ServerManagerActor";
	
	private HashMap<Integer, ActorRef> serverInstanceMap = new HashMap<>();
	
	public ServerManagerActor(List<GameServerConfig> configs){
		if(configs == null || configs.isEmpty()) return;
		for(GameServerConfig config : configs){
			if(config.serverConfig.serverType != ServerType.GAME_SERVER){
				//启动的时候默认要启动的GameServer配置错误，则启动失败
				throw new IllegalArgumentException("ServerManagerActor can not start a server which is not game server");
			}
			ActorRef newServer = this.getContext().actorOf(Props.create(ServerActor.class, config), String.valueOf(config.serverConfig.serverId));
			serverInstanceMap.put(config.serverConfig.serverId, newServer);
		}
	}
	
	@Override
	public SupervisorStrategy supervisorStrategy(){
		return new OneForOneStrategy(SupervisorStrategy.makeDecider(new Function<Throwable, SupervisorStrategy.Directive>() {			
			@Override
			public Directive apply(Throwable e) throws Exception {
				log.error("Actor {} error arised.", ServerManagerActor.this.getSelf().path().toString(), e);
				if(e instanceof ActorInitializationException) return SupervisorStrategy.stop();
				if(e instanceof Exception) return SupervisorStrategy.resume();
				return SupervisorStrategy.escalate();
			}
		}));
	}

	@Override
	public void onReceive(Object msg){
		// ServerManagerActor是GameServer最根部的Actor，当它出现错误时不能让它的监管对其重启，因此
		// 在这里吞掉了所有的Exception。当然，除Exception外的Throwable发生时，一定是发生了我们无法
		// 控制的错误，此时停止整个服务器的行为是可以接受的。
		try{
			if(msg instanceof StartNewServer){
				startNewServer((StartNewServer)msg);
			}else{
				unhandled(msg);
			}
		}catch(Exception e){
			log.error("ServerManagerActor arises exception", e);
		}
		
	}

	private void startNewServer(StartNewServer msg) {
		if(msg.newServerConfig == null){
			this.getSender().tell(new StartNewServer.Failed("ServerManagerActor can not start new server with null config"), this.getSelf());
		}else if(msg.newServerConfig.serverType != ServerType.GAME_SERVER){
			this.getSender().tell(new StartNewServer.Failed("ServerManagerActor can not start new server which is not game server"), this.getSelf());
		}else{
			ActorRef newServer = this.getContext().actorOf(Props.create(ServerActor.class, msg.newServerConfig), String.valueOf(msg.newServerConfig.serverId));
			serverInstanceMap.put(msg.newServerConfig.serverId, newServer);
			this.getSender().tell(new StartNewServer.Succeed(newServer), getSelf());
		}
	}

}
