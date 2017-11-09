package com.mokylin.bleach.core.isc.remote.actorrefs;

import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

/**
 * 代表组装好的的ActorRef的接口。
 * 
 * @author pangchong
 *
 */
public interface IActorPackages {

	public void sendMessage(Object sendingMsg, MessageTarget target);
	
	public ServerType getServerType();
	
	public int getServerId();
}
