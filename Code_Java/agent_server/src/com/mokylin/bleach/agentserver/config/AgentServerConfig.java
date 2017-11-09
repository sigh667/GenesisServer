package com.mokylin.bleach.agentserver.config;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mokylin.bleach.core.akka.config.AkkaConfig;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.config.model.NetInfo;
import com.mokylin.bleach.core.isc.ServerType;
import com.typesafe.config.Config;

public class AgentServerConfig {

	/**本Server的配置信息*/
	public final ServerConfig serverConfig;
	/**LoginServer的信息*/
	public final ServerConfig loginServerConfig;

	/**监听客户端的IP端口信息*/
	public final NetInfo netInfoToClient;
	/**监听MapServer的IP端口信息*/
	public final NetInfo netInfoToMapServer;

	public AgentServerConfig(ServerConfig sConfig, NetInfo netInfoToClient, NetInfo netInfoToMapServer, ServerConfig loginServerConfig) {
		this.serverConfig = checkNotNull(sConfig);
		this.netInfoToClient = netInfoToClient;
		this.netInfoToMapServer = netInfoToMapServer;
		this.loginServerConfig = loginServerConfig;
	}

	private static Config config = ConfigBuilder.buildConfigFromFileName("AgentServer.conf");

	private static AkkaConfig getAkkaConfig(){
		return new AkkaConfig(config.getConfig("agentServer.akka"));
	}

	private static NetInfo getNetInfoToClient() {
		NetInfo netInfo = new NetInfo();
		netInfo.setHost(config.getString("agentServer.hostToClient"));
		netInfo.setPort(config.getInt("agentServer.portToClient"));
		return netInfo;
	}
	private static NetInfo getNetInfoToMapServer() {
		NetInfo netInfo = new NetInfo();
		netInfo.setHost(config.getString("agentServer.hostToMapServer"));
		netInfo.setPort(config.getInt("agentServer.portToMapServer"));
		return netInfo;
	}
	/**
	 * 获取连接的LoginServer的配置信息，包括ID，host以及port
	 * 
	 * @return
	 */
	public static ServerConfig getConnectedLoginConfig() {
		Config tempConfig = config.getConfig("agentServer.connectTo.loginServer");
		return new ServerConfig(ServerType.LOGIN_SERVER,
				tempConfig.getInt("id"), new AkkaConfig(
						tempConfig.getString("host"),
						tempConfig.getInt("port")));
	}

	public static AgentServerConfig getAgentServerConfig(){
		return new AgentServerConfig(new ServerConfig(ServerType.AGENT_SERVER, config.getInt("agentServer.serverId"), getAkkaConfig()), 
				getNetInfoToClient(),
				getNetInfoToMapServer(),
				getConnectedLoginConfig());
	}
}
