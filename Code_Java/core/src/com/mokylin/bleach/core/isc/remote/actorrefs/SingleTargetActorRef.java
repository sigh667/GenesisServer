package com.mokylin.bleach.core.isc.remote.actorrefs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

import akka.actor.ActorRef;

/**
 * 单个ActorRef的组装类。<p>
 * 
 * 该类中所持有的是单个的ActorRef，当发送消息给此ActorRef时，该类不会
 * 判断消息的目的地，而是直接将消息发送给Actor。
 * 
 * @author pangchong
 */
public class SingleTargetActorRef implements IActorPackages, Serializable {
	
	private static final long serialVersionUID = 1L;

	private final ActorRef serverActor;
	
	private final ServerType sType;
	
	private final int sId;
	
	public SingleTargetActorRef(ServerType sType, int sId, ActorRef serverActor){
		this.sType = checkNotNull(sType, "ServerMsgActorRef can not init with null server type!");
		this.sId = sId;
		this.serverActor = checkNotNull(serverActor, "ServerMsgActorRef can not init with null server actor!");
	}

	@Override
	public void sendMessage(Object sendingMsg, MessageTarget target) {
		serverActor.tell(sendingMsg, ActorRef.noSender());
	}

	@Override
	public ServerType getServerType() {
		return this.sType;
	}

	@Override
	public int getServerId() {
		return this.sId;
	}

}
