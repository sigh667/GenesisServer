package com.mokylin.bleach.core.isc.remote;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.core.isc.msg.ServerMessage;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;

/**
 * 远程服务器在本地的表示对象。<p>
 * 
 * 该对象会在创建时持有一个远程ActorRef的引用，之后利用该ActorRef进行消息的发送。每一个通过
 * RemoteServer发送的消息都将会被包装成一个{@link ServerMessage}。<p>
 * 
 * 当远程服务器关闭时，该对象将收到关闭通知从而关闭，关闭后的RemoteServer对象将无法向外发送消息，直到远程服务器
 * 通过某种方式重新建立连接。
 * 
 * @author pangchong
 *
 */
public class RemoteServer implements IRemote {
	
	private final Logger log = LoggerFactory.getLogger(RemoteServer.class);
	
	private IActorPackages remoteActor = null;
	
	private ServerConfig localConfig = null;
	
	private volatile boolean isShutdown = true;

	public RemoteServer(IActorPackages remoteActor, ServerConfig localConfig) {
		this.remoteActor = checkNotNull(remoteActor, "RemoteServer can not reference a null remote actor!");
		this.localConfig = checkNotNull(localConfig, "RemoteServer can not init with a null local config!");
		this.isShutdown = false;
	}
	
	/**
	 * 关闭当前RemoteServer。关闭后消息将不能发送，isShutdown()方法将返回true。
	 */
	void shutdown() {
		this.isShutdown = true;
	}
	
	IActorPackages getRemoteActorRef(){
		return this.remoteActor;
	}
	
	/**
	 * 将消息包装成{@link ServerMessage}发送到远端服务器。
	 * 
	 * @param message
	 */
	@Override
	public void sendMessage(IMessage message){
		if(!isShutdown){
			remoteActor.sendMessage(wrap(message), message.getTarget());
			return;
		}
		
		log.warn("Can not send message to remote cause remote server has shut down!");
	}
	
	/**
	 * 远端服务器是否已经关闭。
	 * 
	 * @return
	 */
	@Override
	public boolean isShutdown(){
		return isShutdown;
	}
	
	/**
	 * 获取远程服务器的ServerType。
	 * 
	 * @return
	 */
	@Override
	public ServerType getServerType(){
		return this.remoteActor.getServerType();
	}
	
	/**
	 * 获取远程服务器的ServerId。
	 * 
	 * @return
	 */
	@Override
	public int getServerId(){
		return this.remoteActor.getServerId();
	}

	/**
	 * 将消息包装成{@link ServerMessage}对象。
	 * 
	 * @param message
	 * @return
	 */
	private ServerMessage wrap(Object message) {
		return new ServerMessage(localConfig.serverType, localConfig.serverId, message);
	}
}
