package com.mokylin.bleach.gameserver.core.heartbeat;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;

import com.mokylin.bleach.gameserver.core.global.Globals;

/**
 * 除PlayerActor外全局心跳帮助类。<p>
 * 
 * <b>注意：该类不能用于PlayerActor的心跳注册。</b>
 * 
 * @author pangchong
 *
 */
public class HeartbeatHelper {
	
	private static long HEART_BEAT_GAP = 500;

	/**
	 * 注册一个心跳消息，指定的milliseconds后触发。
	 * 
	 * @param heartbeatActor
	 * @param milliseconds
	 */
	public static void registerHeartbeat(ActorRef heartbeatActor, long milliseconds){
		Globals.getScheduleProcessUnit().scheduleOnce(heartbeatActor, Heartbeat.INSTANCE, milliseconds, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 注册一个心跳消息，500毫秒触发。
	 * 
	 * @param heartbeatActor
	 */
	public static void registerHeartbeat(ActorRef heartbeatActor){
		registerHeartbeat(heartbeatActor, HEART_BEAT_GAP);
	}
}
