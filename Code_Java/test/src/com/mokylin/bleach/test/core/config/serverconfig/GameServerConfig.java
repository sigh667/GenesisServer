package com.mokylin.bleach.test.core.config.serverconfig;


/**
 * 服务器配置
 * 
 * @author yaguang.xiao
 * 
 */
public class GameServerConfig {
	private String serverName = "";
	private String host = "";
	private int port = 2233;
	private LogConfig logConfig;

	public String getServerName() {
		return serverName;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public LogConfig getLogConfig() {
		return logConfig;
	}
	
	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append(" {\n");
		content.append("\tserverName = " + serverName + "\n");
		content.append("\thost = " + host + "\n");
		content.append("\tport = " + port + "\n");
		content.append("\tlogConfig" + new StringBuilder(new StringBuilder(logConfig.toString().replaceAll("\n", "\n\t")).reverse().toString().replaceFirst("\t", "")).reverse().toString());
		content.append("}\n");
		return content.toString();
	}
}
