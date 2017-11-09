package com.mokylin.bleach.core.isc.remote;

import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.core.isc.msg.ServerMessage;

/**
 * 用于表示远端服务器的接口。<p>
 * 
 * 当服务器收到来自远端服务器的消息时，一个该接口的实现会用来表示远端的服务器。
 * 该接口封装了和远端服务器通信所用到的全部功能接口。<p>
 * 
 * 对于服务器间的大部分通信，使用的都是fire and forgot方式进行消息发送.
 * 
 * @author pangchong
 *
 */
public interface IRemote {
	
	/**
	 * 获取远端服务器的ServerId。
	 * 
	 * @return
	 */
	public int getServerId();
	
	/**
	 * 获取远端服务器的ServerType。
	 * 
	 * @return
	 */
	public ServerType getServerType();
	
	/**
	 * 远端服务器是否已经关闭。<p>
	 * 
	 * <b>当远端服务器关闭时，所有发向此服务器的消息将被本地抛弃。</b>
	 * 
	 * @return
	 */
	public boolean isShutdown();
	
	/**
	 * 将消息包装成{@link ServerMessage}发送到远端服务器。<p>
	 * 
	 * <b>当远端服务器关闭时，此方法将抛弃要发送的消息。</b>
	 * 
	 * @param message
	 */
	public void sendMessage(IMessage message);
}
