package com.mokylin.bleach.core.isc.msg;

import java.io.Serializable;

import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.isc.remote.actorrefs.IActorPackages;

/**
 * 注册本地ActorRef的组装类到远程Server中所使用的消息。
 * 
 * @author pangchong
 *
 */
public class Register implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public final ServerType sourceServerType;
	public final int sourceServerId;
	public final IActorPackages localActorPackages;

	public Register(ServerType serverType, int serverId, IActorPackages localActorPackages) {
		this.sourceServerType = serverType;
		this.sourceServerId = serverId;
		this.localActorPackages = localActorPackages;
	}

}
