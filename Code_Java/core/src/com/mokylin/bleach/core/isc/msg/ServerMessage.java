package com.mokylin.bleach.core.isc.msg;

import com.mokylin.bleach.core.isc.ServerType;

/**
 * 服务器间通信所使用的消息容器。<p>
 * 
 * 在服务期间通信的消息，绝大部分都会被自动包装成该类的对象进行传递。
 * 个别底层所使用的特殊消息除外。<p>
 * 
 * <b>需要注意的是：</b>该消息会使用Kryo进行序列化和反序列化，因此，
 * 像ActorRef这样的复杂对象不能使用此消息进行发送。
 * 
 * @author pangchong
 *
 */
public class ServerMessage{
	
	/** 消息来源的ServerType */
	public final ServerType sType;
	/** 消息来源的Server ID */
	public final int sId;
	/** The message passed by the server */
	public final Object msg;
	
	public ServerMessage(ServerType sType, int sId, Object msg) {
		this.sType = sType;
		this.sId = sId;
		this.msg = msg;
	}
}
