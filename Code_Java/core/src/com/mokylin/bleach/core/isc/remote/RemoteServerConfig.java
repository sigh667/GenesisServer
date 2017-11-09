package com.mokylin.bleach.core.isc.remote;

import com.mokylin.bleach.core.isc.ServerType;

public class RemoteServerConfig {
	/** 服务器类型 */
	public final ServerType serverType;
	/** 服务器的唯一标示Id */
	public final int serverId;
	/** 服务器的Ip */
	public final String host;
	/** 服务器的端口 */
	public final int port;
	
	public RemoteServerConfig(ServerType serverType, int serverId, String host, int port){
		this.serverType = serverType;
		this.serverId = serverId;
		this.host = host;
		this.port = port;
	}
}
