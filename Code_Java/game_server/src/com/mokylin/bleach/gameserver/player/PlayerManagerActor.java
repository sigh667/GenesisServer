package com.mokylin.bleach.gameserver.player;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.concurrent.TimeUnit;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Terminated;

import com.google.common.base.Optional;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.isc.msg.ServerMessage;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.remote.actorrefs.annotation.MessageAcception;
import com.mokylin.bleach.core.net.msg.CSSMessage;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.gameserver.core.actor.StopSupervisorStrategyActor;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.heartbeat.Heartbeat;
import com.mokylin.bleach.gameserver.core.heartbeat.HeartbeatHelper;
import com.mokylin.bleach.gameserver.core.heartbeat.StartHeartbeat;
import com.mokylin.bleach.gameserver.core.humaninfocache.HumanInfoCache;
import com.mokylin.bleach.gameserver.core.log.LogOnlineCountsTask;
import com.mokylin.bleach.gameserver.core.timeout.TimeoutCallbackManager.TimeoutCBWrapper;
import com.mokylin.bleach.gameserver.login.OnlinePlayerService;
import com.mokylin.bleach.gameserver.login.protocol.RemoveHumanInfoMsg;
import com.mokylin.bleach.gameserver.player.protocol.RegisterPlayer;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 用于在线玩家以及玩家上下线管理的Actor。<p>
 * 
 * 该Actor的监管策略为stop。因为该Actor是在线玩家的父Actor，当在线玩家处理逻辑发生
 * 异常的时候，我们无法保证在线玩家的状态是否依然一致，因此将玩家踢下线是最好的处理。
 * 
 * @author pangchong
 *
 */
@MessageAcception(MessageTarget.PLAYER_MANAGER)
public class PlayerManagerActor extends StopSupervisorStrategyActor {
	
	public static final String ACTOR_NAME = "PlayerManager";
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final ServerGlobals sGlobals;
	private final PlayerManagerArgs args;
	private final OnlinePlayerService onlinePlayerService;
	private final TimeAxis<PlayerManagerActor> timeAxis;
	private final HashMap<ActorRef, RegisterPlayer> playerActorRefs;
	
	public PlayerManagerActor(ServerGlobals sGlobals, HumanInfoCache humanInfoCache){
		this.sGlobals = checkNotNull(sGlobals, "PlayerManagerActor can not be created with null ServerGlobals!");
		checkNotNull(humanInfoCache, "PlayerManagerActor can not be created with null human info cache!");
		onlinePlayerService = new OnlinePlayerService(sGlobals);
		playerActorRefs = new HashMap<>();
		args = new PlayerManagerArgs(this.getContext(), humanInfoCache, onlinePlayerService, playerActorRefs);
		timeAxis = new TimeAxis<PlayerManagerActor>(Globals.getTimeService(), this);
		
		//记录日志周期
		long millis = TimeUnit.SECONDS.toMillis(GlobalData.getConstants().getLogOnlineCountsPeriod());
		//运营日志-调度定时任务，记录玩家在线日志
		timeAxis.scheduleEventAfterMS(new LogOnlineCountsTask(sGlobals, onlinePlayerService), millis, millis);
	}
	
	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg instanceof ServerMessage){
			handleServerMessage((ServerMessage) msg);
		}else if(msg instanceof TimeoutCBWrapper){
			sGlobals.getTimeoutCBManager().executeCallBack((TimeoutCBWrapper) msg);
		}else if(msg == Heartbeat.INSTANCE){
			heartbeat();
		}else if(msg instanceof Terminated){
			Terminated tMsg = (Terminated) msg;
			this.context().unwatch(tMsg.actor());
			RegisterPlayer info = playerActorRefs.remove(tMsg.actor());
			onlinePlayerService.removePlayer(info.channel, info.accountId);
		} else if (msg instanceof RemoveHumanInfoMsg) {
			RemoveHumanInfoMsg rMsg = (RemoveHumanInfoMsg)msg;
			HumanInfo humanInfo = rMsg.getHumanInfo();
			args.humanInfoCache.remove(humanInfo, sGlobals);
		} else if(msg == StartHeartbeat.INSTANCE){
			HeartbeatHelper.registerHeartbeat(this.getSelf());
		}else{
			Globals.getServerMsgFuncService().handle(MessageTarget.PLAYER_MANAGER, sGlobals.getAgentServer(), msg, sGlobals, args);
		}
	}

	private void heartbeat() {
		try{
			timeAxis.heartbeat();
		}finally{
			try{ //这里再增加一个try保证下一次的心跳一定能被注册上
				sGlobals.getActorGlobals().letPlayersHeartbeat();
			}finally{
				HeartbeatHelper.registerHeartbeat(this.getSelf());
			}			
		}		
	}

	private void handleServerMessage(ServerMessage sMsg) {
		if(sMsg.msg instanceof CSSMessage){
			CSSMessage csMsg = (CSSMessage) sMsg.msg;
			Optional<Player> playerOption = sGlobals.getSession(csMsg.agentSessionId);
			if(playerOption.isPresent()){
				handleCSSMsg(csMsg, playerOption.get());
			}else{
				log.warn("PlayerManagerActor receive a message [{}] can not find player session: AgentServerId:[{}], ServerType:[{}], ServerId:[{}]",
						csMsg.messageType, csMsg.agentSessionId, sMsg.sType.name(), sMsg.sId);
			}
		}else{
			Optional<IRemote> option = sGlobals.getISCService().getRemote(sMsg.sType, sMsg.sId);
			if(option.isPresent()){
				Globals.getServerMsgFuncService().handle(MessageTarget.PLAYER_MANAGER, option.get(), sMsg.msg, sGlobals, args);
			}else{
				log.warn("PlayerManagerActor receive a message [{}] can not find remote: ServerType:[{}], ServerId:[{}]",
						sMsg.msg.getClass().getName(), sMsg.sType.name(), sMsg.sId);
			}
		}
	}

	private void handleCSSMsg(CSSMessage msg, Player player) {
		if(player.getStatus().isCanProcess(msg.messageType)){
			try{
				Globals.getClientMsgFuncService().handle(MessageTarget.PLAYER_MANAGER, msg.messageType, msg.messageContent, player, sGlobals, args);
			}catch(Exception e){
				log.error("Exception occurs in PlayerManagerActor: Account [{}], Channel [{}] Exception [{}].", player.getAccountId(), player.getChannel(), e);
				player.disconnect();
			}
		}else{
			log.warn("PlayerManagerActor: Wrong message [{}] is received in Player Status: [{}], Player session id: [{}]", 
					msg.messageType, player.getStatus().name(), player.getId());
		}
	}
	
}
