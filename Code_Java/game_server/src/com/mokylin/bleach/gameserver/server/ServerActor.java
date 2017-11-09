package com.mokylin.bleach.gameserver.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorInitializationException;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorContext;
import akka.dispatch.OnComplete;

import com.google.common.base.Optional;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.dailyreward.template.DailyRewardsTemplate;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.msg.ServerMessage;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;
import com.mokylin.bleach.core.isc.remote.actorrefs.MultiTargetActorRefs;
import com.mokylin.bleach.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.util.TimeUtils;
import com.mokylin.bleach.gameserver.arena.ArenaActor;
import com.mokylin.bleach.gameserver.arena.init.ArenaInitResult;
import com.mokylin.bleach.gameserver.core.actor.ResumeSupervisorStrategyActor;
import com.mokylin.bleach.gameserver.core.concurrent.ArgsCallable;
import com.mokylin.bleach.gameserver.core.concurrent.AsyncArgs;
import com.mokylin.bleach.gameserver.core.config.GameServerConfig;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerActorGlobals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.heartbeat.Heartbeat;
import com.mokylin.bleach.gameserver.core.heartbeat.HeartbeatHelper;
import com.mokylin.bleach.gameserver.core.heartbeat.StartHeartbeat;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitComplete;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFailException;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFailed;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFunction;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitObject;
import com.mokylin.bleach.gameserver.player.PlayerManagerActor;
import com.mokylin.bleach.gameserver.scene.SceneActor;
import com.mokylin.bleach.gameserver.server.init.ServerActorInitResult;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 用于逻辑服务器的Actor。<p>
 * 
 * 该Actor的监管策略为resume。因为从服务器的逻辑来说，任何可预测的异常（Exception及其子类）
 * 都不应该停止或者重启其内部系统的继续执行。但是当除了可预测的异常之外的Throwable发生时，
 * 意味着发生了严重的错误，此时停止整个ActorSystem也是可以接受的。
 * 
 * @author pangchong
 *
 */
@MessageAcception(MessageTarget.SERVER)
public class ServerActor extends ResumeSupervisorStrategyActor{
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final GameServerConfig serverConfig;
	private final ServerGlobals sGlobals;
	
	private ActorRef scene = null;
	private ActorRef playerManager = null;
	private ActorRef arena = null;
	private IActorPackages localPackages = null;
	
	public ServerActor(GameServerConfig serverConfig){
		this.serverConfig = serverConfig;
		this.sGlobals = new ServerGlobals(this.serverConfig);
	}
	
	@Override
	public void preStart() throws Exception{
		//初始化本服务器所需的所有全局变量以及全局Actor
		Globals.getServerInitProcessUnit().submitTask(new ArgsCallable<ServerInitObject>() {
			@Override
			public ServerInitObject call(AsyncArgs args) throws Exception {
				//获取启动服务器的全局数据，一般这里需要从redis中获取
				Map<Class<?>, Object> map = new HashMap<>();
				for(Entry<Class<?>, ServerInitFunction<?>> each : Globals.getServerInitFuncService().funcs.entrySet()){
					ServerInitFunction<?> eachFunc = each.getValue();
					map.put(each.getKey(), eachFunc.apply(sGlobals));
				}
				return new ServerInitObject(map);
			}
		}).onComplete(new OnComplete<ServerInitObject>(){
			@Override
			public void onComplete(Throwable failure, ServerInitObject result)
					throws Throwable {
				UntypedActorContext context = ServerActor.this.getContext();
				if(failure != null) {
					context.self().tell(new ServerInitFailed(failure), ActorRef.noSender());
					return;
				}
				
				HashMap<Class<? extends UntypedActor>, ActorRef> map = serverInitCompleteFunction(context, result);
				map.put(ServerManagerActor.class, context.parent());
				//打包发送到ServerActor容器中
				context.self().tell(new ServerInitComplete(map, sGlobals, result), ActorRef.noSender());
			}
		}, Globals.getServerInitProcessUnit().getExecutionContext());
		
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg instanceof ServerInitComplete){
			try{
				//检查服务器启动时间对应的当月奖励
				int currentMonth = TimeUtils.parseMonth(Globals.getTimeService().now());
				if(!GlobalData.getTemplateService().isTemplateExist(currentMonth, DailyRewardsTemplate.class)){
					throw new RuntimeException("Can not find daily rewards of month " + currentMonth + " of server start time!");
				}

				ServerInitComplete complete = (ServerInitComplete) msg;
				
				localPackages = new MultiTargetActorRefs(serverConfig.getServerType(), serverConfig.getServerId(), Globals.getTargetClassMap(), complete.map);
				
				playerManager = getActorRefByType(complete, PlayerManagerActor.class);
				scene = getActorRefByType(complete, SceneActor.class);
				arena = getActorRefByType(complete, ArenaActor.class);
				ServerActorGlobals actorGlobals = new ServerActorGlobals(playerManager, scene, arena);
				sGlobals.setActorGlobals(actorGlobals);
				
				// 启动时间轴
				HeartbeatHelper.registerHeartbeat(this.getSelf());
				// 给ServerGlobals进行赋值
				for(Entry<Class<?>, ServerInitFunction<?>> each : Globals.getServerInitFuncService().funcs.entrySet()){
					ServerInitFunction<?> eachFunc = each.getValue();
					eachFunc.set(complete.serverInitObject.get(each.getKey()), sGlobals);
				}
				
				//初始化完毕，通知PlayerManagerActor开始心跳
				sGlobals.getActorGlobals().playerManager.tell(StartHeartbeat.INSTANCE);
				
				//注册本服到DataServer
				for(int each : serverConfig.dataServerIds){
					registerToRemote(ServerType.DATA_SERVER, each);
					sGlobals.getDataService().add(each, sGlobals.getISCService().getRemote(ServerType.DATA_SERVER, each).get());
				}
				
				//注册本服到AgentServer
				registerToRemote(ServerType.AGENT_SERVER, serverConfig.getConnectedAgentServerId());
				log.info("Server {} has started!", this.sGlobals.getServerId());
			}catch(Exception e){
				// 停止服务器
				throw new ActorInitializationException(this.self(), "Server init failed! ID: ["+ sGlobals.getServerId() +"]", e);
			}
		} else if (msg instanceof ServerInitFailed){
			throw new ActorInitializationException(this.self(), "Server init failed! ID: ["+ sGlobals.getServerId() +"]", ((ServerInitFailed)msg).getException());
		} else if (msg instanceof ServerMessage) {
			this.handleServerMsg((ServerMessage) msg);
		} else if (msg == Heartbeat.INSTANCE){
			heartbeat();
		}
	}
	
	/**
	 * 心跳
	 */
	private void heartbeat() {
		try{
			this.sGlobals.getTimeAxis().heartbeat();
		}finally{
			HeartbeatHelper.registerHeartbeat(this.getSelf());		
		}		
	}
	
	private void handleServerMsg(ServerMessage sMsg) {
		Optional<IRemote> option = sGlobals.getISCService().getRemote(sMsg.sType, sMsg.sId);
		if(option.isPresent()){
			Globals.getServerMsgFuncService().handle(MessageTarget.SERVER, option.get(), sMsg.msg, sGlobals, MsgArgs.nullArgs);
		}else{
			log.warn("ServerActor receive a message [{}] can not find remote: ServerType:[{}], ServerId:[{}]",
					sMsg.msg.getClass().getName(), sMsg.sType.name(), sMsg.sId);
		}
	}
	
	private void registerToRemote(ServerType sType, int sId){
		if(sGlobals.getISCService().registerToRemote(sType, sId, localPackages)) return;
		throw new RuntimeException("Can not register to server, Type: ["+ sType +"], ID: [" + sId + "]");
	}
	
	private ActorRef getActorRefByType(ServerInitComplete complete, Class<? extends UntypedActor> actorType){
		Optional<ActorRef> option = complete.get(actorType);
		if(!option.isPresent()){
			throw new ServerInitFailException(actorType.getName() + " init failed!");
		}
		return option.get();
	}


	/**
	 * 根据获取的数据，对该逻辑游戏服务器的全局Actor进行启动。
	 * 
	 * @param context
	 * @param result
	 * @param redisProcessUnit 
	 * @return
	 */
	private HashMap<Class<? extends UntypedActor>, ActorRef> serverInitCompleteFunction(UntypedActorContext context, ServerInitObject result) {
		ServerActorInitResult serverInitResult = result.get(ServerActorInitResult.class).get();
		HashMap<Class<? extends UntypedActor>, ActorRef> map = new HashMap<>();
		map.put(ServerActor.class, context.self());
		map.put(PlayerManagerActor.class, context.actorOf(Props.create(PlayerManagerActor.class, sGlobals, serverInitResult.humanInfoCache), PlayerManagerActor.ACTOR_NAME));
		map.put(SceneActor.class, context.actorOf(Props.create(SceneActor.class, sGlobals), SceneActor.ACTOR_NAME));
		map.put(ArenaActor.class, context.actorOf(Props.create(ArenaActor.class, sGlobals, result.get(ArenaInitResult.class).get()), ArenaActor.ACTOR_NAME));
		return map;
	}

}
