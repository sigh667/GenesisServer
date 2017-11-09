package com.mokylin.bleach.core.isc;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.msg.Register;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.isc.remote.RemoteServer;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class ISCService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final RemoteActorManager remoteActorManager;
	
	private final ServerConfig localConfig;
	
	private final Table<ServerType, Integer, IRemote> remoteTable;
	
	public ISCService(RemoteActorManager remoteActorManager, ServerConfig localConfig){
		this.remoteActorManager = checkNotNull(remoteActorManager, "ISCService can not init with a null remote actor manager!");
		this.localConfig = checkNotNull(localConfig, "ISCService can not init with a null local config!");
		remoteTable = HashBasedTable.create();
	}
	
	void addRemote(ServerType sType, int sId, IRemote remoteServer){
		synchronized (remoteTable) {
			this.remoteTable.put(sType, sId, remoteServer);
		}		
	}
	
	/**
	 * 向远端服务器进行双向注册，注册完毕后，本地以及远端的服务器都会持有对方的RemoteServer对象，在之后可以直接通过
	 * RemoteServer进行通信。如果远端服务器已经注册，则该方法什么都不会做。<p>
	 * 
	 * <b>注意：该方法是阻塞方法，使用时要避免在对响应敏感的位置调用。</b><p>
	 * 
	 * @param remoteServerConfig
	 * @return
	 */
	public boolean registerToRemote(ServerType sType, int sId, IActorPackages localActorPackages, MessageTarget... target){
		checkNotNull(sType, "ISCService can not registerToRemote with null server type!");
	
		if(remoteTable.contains(sType, sId)) return true;
		
		Optional<IActorPackages> option = remoteActorManager.getRemoteActorRef(sType, sId);
		if(!option.isPresent()){
			log.warn("Server {} - {} should be connected before register.", sType, sId);
			return false;
		}
		
		Register register = new Register(localConfig.serverType, localConfig.serverId, localActorPackages);
		
		MessageTarget defaultTarget = MessageTarget.ISC_ACTOR;
		if(target != null && target.length != 0 && target[0] != null){
			defaultTarget = target[0];
		}
		option.get().sendMessage(register, defaultTarget);
		RemoteServer remote = new RemoteServer(option.get(), localConfig);
		
		synchronized (remoteTable) {
			IRemote rs = remoteTable.get(sType, sId);
			if(rs != null) return true;
			remoteTable.put(sType, sId, remote);
		}
		return true;
	}
	
	public ServerConfig getLocalConfig(){
		return this.localConfig;
	}
	
	public Optional<IRemote> getRemote(ServerType sType, int sId){
		return Optional.fromNullable(remoteTable.get(sType, sId));
	}
}
