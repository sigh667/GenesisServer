package com.mokylin.bleach.core.isc.remote;

import com.mokylin.bleach.core.isc.msg.Connect;
import com.mokylin.bleach.core.isc.msg.ConnectResult;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;

import akka.actor.ActorRef;

/**
 * 用于响应{@link Connect}消息的通用类，该消息的响应逻辑
 * 仅仅把本地组装好的ActorRefs发送给远程。
 * 
 * @author pangchong
 *
 */
public abstract class RemoteConnectFunc {
	
	public boolean apply(Object msg, ActorRef sender){
		if(msg == Connect.INSTANCE){
			sender.tell(new ConnectResult(assembleActorRefs()), ActorRef.noSender());
			return true;
		}
		return false;
	}
	
	/**
	 * 组装本地ActorRefs的方法。
	 * 
	 * @return
	 */
	public abstract IActorPackages assembleActorRefs();
}
