package com.mokylin.bleach.core.config.model;

import com.mokylin.bleach.core.isc.ServerType;

/**
 * 服务器信息
 * @author baoliang.shen
 *
 */
public class ServerInfo {

	private ServerType serverType;
	private int serverID;
	private int serverGroup;
	private String host;
	private int port;

	public ServerType getServerType() {
		return serverType;
	}
	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}
	public int getServerID() {
		return serverID;
	}
	public void setServerID(int serverID) {
		this.serverID = serverID;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getServerGroup() {
		return serverGroup;
	}
	public void setServerGroup(int serverGroup) {
		this.serverGroup = serverGroup;
	}
}
