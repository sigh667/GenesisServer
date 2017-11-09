package com.mokylin.bleach.gameserver.core.actor;

import static com.google.common.base.Preconditions.checkNotNull;
import akka.actor.ActorRef;

/**
 * ActorRef的包装类，方便调用者直接发送消息，而不用在显式的指定ActorRef.noSender。
 * 
 * @author pangchong
 *
 */
public class ActorWrapper {
	
	public final ActorRef actorRef;
	
	public ActorWrapper(ActorRef actorRef) {
		this.actorRef = checkNotNull(actorRef);
	}

	/**
	 * 发送任意消息到指定的Actor，发送方为noSender。
	 * 
	 * @param msg
	 */
	public void tell(Object msg){
		actorRef.tell(msg, ActorRef.noSender());
	}
}
