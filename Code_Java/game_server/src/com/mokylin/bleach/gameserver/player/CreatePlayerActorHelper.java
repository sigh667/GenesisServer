package com.mokylin.bleach.gameserver.player;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.mokylin.bleach.gamedb.human.HumanData;
import com.mokylin.bleach.gamedb.human.HumanInfo;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.player.protocol.RegisterPlayer;

/**
 * 创建PlayerActor的帮助类。<p>
 * 
 * 该类的方法只在PlayerManagerActor中调用。
 * 
 * @author pangchong
 *
 */
public class CreatePlayerActorHelper {
	
	public static String createPlayerActorName(String channel, String accountId){
		return channel + ":::" + accountId;
	}

	/**
	 * 根据Human的详细数据HumanData来创建PlayerActor。<p>
	 * 
	 * <b>该方法只在PlayerManagerActor中调用。</b>
	 * 
	 * @param player
	 * @param humanData
	 * @param sGlobals
	 * @param playerManagerArgs
	 */
	public static void createPlayerActor(Player player, HumanData humanData, ServerGlobals sGlobals, PlayerManagerArgs playerManagerArgs){
		player.setUuid(humanData.humanId);
		ActorRef ar = playerManagerArgs.context.actorOf(Props.create(PlayerActor.class, player, sGlobals), createPlayerActorName(player.getChannel(), player.getAccountId()));
		ar.tell(humanData, ActorRef.noSender());
		playerManagerArgs.context.watch(ar);
		playerManagerArgs.playerActorRefs.put(ar, new RegisterPlayer(player.getChannel(), player.getAccountId()));
		sGlobals.getActorGlobals().addPlayerActor(humanData.humanId, ar);
		player.setStatus(LoginStatus.Gaming);
	}
	
	/**
	 * 根据Human的简要数据HumanInfo来创建PlayerActor。<p>
	 * 
	 * <b>该方法只在PlayerManagerActor中调用。</b>
	 * 
	 * @param player
	 * @param humanInfo
	 * @param sGlobals
	 * @param playerManagerArgs
	 */
	public static void createPlayerActor(Player player, HumanInfo humanInfo, ServerGlobals sGlobals, PlayerManagerArgs playerManagerArgs){
		player.setUuid(humanInfo.getId());
		ActorRef ar = playerManagerArgs.context.actorOf(Props.create(PlayerActor.class, player, sGlobals), createPlayerActorName(player.getChannel(), player.getAccountId()));
		ar.tell(humanInfo, ActorRef.noSender());
		playerManagerArgs.context.watch(ar);
		playerManagerArgs.playerActorRefs.put(ar, new RegisterPlayer(player.getChannel(), player.getAccountId()));
		sGlobals.getActorGlobals().addPlayerActor(humanInfo.getId(), ar);
		player.setStatus(LoginStatus.Gaming);
	}
}
