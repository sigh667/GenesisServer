package com.mokylin.bleach.core.isc.remote.actorrefs;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 多个ActorRef的组装类。<p>
 * 
 * 该类中所持有的是多个的ActorRef，使用MessageTarget进行区分，当发送消息时，
 * 该类会使用MessageTarget来获取对应的ActorRef，从而将消息发送给指定的Actor。
 * 
 * @author pangchong
 */
public class MultiTargetActorRefs implements IActorPackages, Serializable {
	
	private static final long serialVersionUID = 1L;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final ServerType sType;
	private final int sId;
	
	private ImmutableMap<MessageTarget, ActorRef> targetActor = null;
	
	public MultiTargetActorRefs(ServerType sType, int sId, Map<MessageTarget, Class<? extends UntypedActor>> targetClassMap, Map<Class<? extends UntypedActor>, ActorRef> targetActorRef){
		checkNotNull(sType, "MultiTargetActorRefs can not init with null server type!");
		checkArgument(targetClassMap!=null && targetClassMap.size()>0, "MultiTargetActorRefs can not init with empty targetClassMap!");
		checkArgument(targetActorRef!=null && targetActorRef.size()>0, "MultiTargetActorRefs can not init with empty targetActorRef!");
		this.sType = sType;
		this.sId = sId;
		
		Builder<MessageTarget, ActorRef> builder = ImmutableMap.builder();
		for(Entry<MessageTarget, Class<? extends UntypedActor>> each : targetClassMap.entrySet()){
			ActorRef actorRef = targetActorRef.get(each.getValue());
			if(actorRef == null) throw new RuntimeException("MessageTarget [" + each.getKey() + "] map to Actor Class [" + each.getValue().getName() + "], but can not find corresponding ActorRef!");
			builder.put(each.getKey(), actorRef);
		}
		targetActor = builder.build();
	}

	@Override
	public void sendMessage(Object sendingMsg, MessageTarget target) {
		ActorRef actor = targetActor.get(target);
		if(actor != null){
			actor.tell(sendingMsg, ActorRef.noSender());
		}else{
			log.warn("Can not find MessageTarget [{}] for Message [{}]", target.name(), sendingMsg.getClass().getName());
		}
	}

	@Override
	public ServerType getServerType() {
		return sType;
	}

	@Override
	public int getServerId() {
		return sId;
	}

}
