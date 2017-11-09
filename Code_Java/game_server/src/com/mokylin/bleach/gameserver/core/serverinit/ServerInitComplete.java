package com.mokylin.bleach.gameserver.core.serverinit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;

/**
 * 游戏逻辑服务器初始化完毕的消息.<p>
 * 
 * 该消息在服务器启动时，在服务器启动线程中创建完毕，发往ServerActor使用。
 * @author pangchong
 *
 */
public class ServerInitComplete {

	public final ImmutableMap<Class<? extends UntypedActor>, ActorRef> map;
	
	public final ServerGlobals sGlobals;
	
	public final ServerInitObject serverInitObject;
	
	public ServerInitComplete(Map<Class<? extends UntypedActor>, ActorRef> map, ServerGlobals sGlobals, ServerInitObject serverInitObject){
		this.map = ImmutableMap.copyOf(checkNotNull(map, "ServerInitComplete can not be created with null map!"));
		this.sGlobals = checkNotNull(sGlobals, "ServerInitComplete can not be created with null server globals!");
		this.serverInitObject = serverInitObject;
	}
	
	public Optional<ActorRef> get(Class<? extends UntypedActor> actorType){
		return Optional.fromNullable(map.get(actorType));
	}
}
