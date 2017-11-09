package com.mokylin.bleach.gameserver.core.global;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.ConcurrentHashMap;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;

import com.mokylin.bleach.gameserver.core.actor.ActorWrapper;
import com.mokylin.bleach.gameserver.core.actor.PlayerActorWrapper;

/**
 * 游戏逻辑服务器的全局所有的ActorRef的引用类。<p>
 * 
 * 该类在PlayerManagerActor中创建，创建之后供整个逻辑游戏服使用。<p>
 * 
 * <b>这里需要注意的是：</b><br>
 * 1. 该对象中所有暴露的公共字段都可以直接访问，不用区分当前是在哪个Actor中；<br>
 * 2. 如果新增加了一个公共的Actor，并且其它Actor会和它进行交互，则需要在这里增加一个公共字段用来引用该ActorRef；<br>
 * 3. 该对象中提供的所有方法只能在PlayerManagerActor中调用，如果在别的地方调用了，男的砍小JJ，女的砍小MM。
 * 
 * @author pangchong
 *
 */
public class ServerActorGlobals {
	
	public final ActorWrapper playerManager;
	
	public final ActorWrapper scene;
	
	public final ActorWrapper arena;
	
	/** 玩家UUID->玩家ActorRef的映射类 */
	private final ConcurrentHashMap<Long, PlayerActorWrapper> playerActors = new ConcurrentHashMap<>();
	
	public ServerActorGlobals(ActorRef playerManager, ActorRef scene, ActorRef arena){
		this.playerManager = new ActorWrapper(checkNotNull(playerManager));
		this.scene = new ActorWrapper(checkNotNull(scene));
		this.arena = new ActorWrapper(checkNotNull(arena));
	}
	
	/**
	 * 增加一个UUID到玩家ActorRef的映射。<p>
	 * 
	 * <b>该方法只允许在PlayerManagerActor中调用</b>
	 * 
	 * @param uuid
	 * @param playerActor
	 */
	public void addPlayerActor(long uuid, ActorRef playerActor){
		if(playerActor == null) return;
		playerActors.put(uuid, new PlayerActorWrapper(playerActor));
	}
	
	/**
	 * 通知所有的玩家进行心跳。<p>
	 * 
	 * <b>该方法只允许在PlayerManagerActor中调用</b>
	 */
	public void letPlayersHeartbeat(){
		for(PlayerActorWrapper each : playerActors.values()){
			each.heartbeat();
		}
	}

	/**
	 * 已经登录完毕的玩家下线时使用。停止一个玩家的PlayerActor。<p>
	 * 
	 * <b>该方法只允许在PlayerManagerActor中调用</b>
	 * @param uuid
	 */
	public void letPlayerStop(long uuid) {
		PlayerActorWrapper player = playerActors.remove(uuid);
		if(player == null) return;
		player.tell(PoisonPill.getInstance());
	}
	
}
