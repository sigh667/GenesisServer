package com.mokylin.bleach.core.akka;

import static com.google.common.base.Preconditions.checkNotNull;
import akka.actor.ActorSystem;

import com.mokylin.bleach.core.akka.config.AkkaConfig;
import com.mokylin.bleach.core.akka.config.AkkaConfigBuilder;

public class Akka {
	
	public final static String ACTOR_SYSTEM_NAME = "ActorSystem";
	
	@SuppressWarnings("unused")
	private final AkkaConfig akkaConfig;
	
	private final ActorSystem actorSystem;
	
	
	public Akka(AkkaConfig akkaConfig){
		checkNotNull(akkaConfig, "Akka can not init with null config");
		this.akkaConfig = akkaConfig;
		actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME, AkkaConfigBuilder.build(akkaConfig.ip, akkaConfig.port).resolve());
	}
	
	public ActorSystem getActorSystem(){
		return actorSystem;
	}	
	
}
