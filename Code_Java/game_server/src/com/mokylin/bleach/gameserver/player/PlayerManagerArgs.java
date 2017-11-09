package com.mokylin.bleach.gameserver.player;

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActorContext;

import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.gameserver.core.humaninfocache.HumanInfoCache;
import com.mokylin.bleach.gameserver.login.LoginService;
import com.mokylin.bleach.gameserver.login.OnlinePlayerService;
import com.mokylin.bleach.gameserver.player.protocol.RegisterPlayer;

/**
 * PlayerManagerActor中各个函数对象所使用的参数对象。<p>
 * 
 * <b>注意：该对象在每个逻辑游戏服务内，仅在PlayerManagerActor中创建一次，
 * 也仅在PlayerManagerActor中使用。</b>
 * 
 * @author pangchong
 *
 */
public class PlayerManagerArgs implements MsgArgs {
	
	public final UntypedActorContext context;
	public final HumanInfoCache humanInfoCache;
	public final OnlinePlayerService onlinePlayerService;
	public final LoginService loginService;
	public final HashMap<ActorRef, RegisterPlayer> playerActorRefs;

	public PlayerManagerArgs(UntypedActorContext context, HumanInfoCache humanInfoCache, OnlinePlayerService onlinePlayerService, HashMap<ActorRef, RegisterPlayer> map) {
		this.context = context;
		this.humanInfoCache = humanInfoCache;
		this.onlinePlayerService = onlinePlayerService;
		this.loginService = new LoginService();
		this.playerActorRefs = map;
	}

}
