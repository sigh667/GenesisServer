package com.mokylin.bleach.core.isc.msg;

import java.io.Serializable;

import com.mokylin.bleach.core.isc.ServerType;

/**
 * 用于向Remote Actor发送ActorRef时使用的消息。<p>
 * 
 * 在远程Actor的通信中，消息优先使用Kryo来进行序列化和反序列化。但是对于消息中
 * 含有ActorRef为内容的，Kryo对其序列化会产生异常，对于这一类消息必须使用标准
 * 的java序列化。
 * 
 * @author pangchong
 *
 */
public class ActorRefMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public final ServerType sourceServerType;
	public final int sourceServerId;
	public final Object msg;
	
	public ActorRefMessage(ServerType sType, int sId, Object msg){
		this.sourceServerType = sType;
		this.sourceServerId = sId;
		this.msg = msg;
	}
}
