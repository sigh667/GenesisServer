package com.mokylin.bleach.remotelogserver;

public class RemoteLogServerConfig {

	private int serverId;
	private String ip;
	private int port;
	/** 用于接收服务器消息的端口 */
	private int akkaPort;
	/** 日志路径 */
	private String logPath;
	/** 游戏名称 */
	private String gameName;
	/** 日志存活时间（单位：分钟） */
	private int logLifeTime;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getAkkaPort() {
		return akkaPort;
	}

	public void setAkkaPort(int akkaPort) {
		this.akkaPort = akkaPort;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public int getLogLifeTime() {
		return logLifeTime;
	}

	public void setLogLifeTime(int logLifeTime) {
		this.logLifeTime = logLifeTime;
	}

}
