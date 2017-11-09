package com.mokylin.bleach.gameserver.core.actor;

import com.mokylin.bleach.gameserver.core.heartbeat.PlayerHeartbeat;

import akka.actor.ActorRef;

/**
 * Player所表示的ActorRef的包装类，增加了通知心跳的方法。
 * 
 * @author pangchong
 *
 */
public class PlayerActorWrapper extends ActorWrapper {

	private final PlayerHeartbeat heartbeat;
	
	public PlayerActorWrapper(ActorRef actorRef) {
		super(actorRef);
		this.heartbeat = new PlayerHeartbeat();
	}
	
	/**
	 * 通知该玩家的Actor进行心跳。<p>
	 * 
	 * <b>该方法只允许在PlayerManagerActor中调用</b>
	 */
	public void heartbeat(){
		if(heartbeat.isDone()){
			heartbeat.reset();
			this.tell(heartbeat);
		}		
	}

}
