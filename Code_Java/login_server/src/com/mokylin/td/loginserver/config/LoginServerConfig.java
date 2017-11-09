package com.mokylin.td.loginserver.config;

import static com.google.common.base.Preconditions.checkNotNull;

import com.mokylin.bleach.core.akka.config.AkkaConfig;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.config.model.NetInfo;
import com.mokylin.bleach.core.isc.ServerType;
import com.typesafe.config.Config;

public class LoginServerConfig {
	/**本架构中，每个java服务器都需要的配置*/
	public final ServerConfig serverConfig;
	/**监听客户端的IP端口信息*/
	public final NetInfo netInfoToClient;

	/**是否为本地认证方式*/
	private boolean localAuth;


	public LoginServerConfig(ServerConfig sConfig, NetInfo netInfoToClient, boolean localAuth) {
		this.serverConfig = checkNotNull(sConfig);
		this.netInfoToClient = checkNotNull(netInfoToClient);
		this.localAuth = localAuth;
	}

	public boolean isLocalAuth() {
		return localAuth;
	}

	/**
	 * 加载配置文件
	 * @return
	 */
	public static LoginServerConfig loadConfig(){
		return new LoginServerConfig(new ServerConfig(ServerType.LOGIN_SERVER, config.getInt("loginServer.serverId"), getAkkaConfig()), 
				loadNetInfoToClient(),
				loadLocalAuth());
	}

	private static boolean loadLocalAuth() {
		return config.getBoolean("loginServer.localAuth");
	}

	private static Config config = ConfigBuilder.buildConfigFromFileName("LoginServer.conf");
	private static AkkaConfig getAkkaConfig(){
		return new AkkaConfig(config.getConfig("loginServer.akka"));
	}
	private static NetInfo loadNetInfoToClient() {
		NetInfo netInfo = new NetInfo();
		netInfo.setHost(config.getString("loginServer.hostToClient"));
		netInfo.setPort(config.getInt("loginServer.portToClient"));
		return netInfo;
	}

	/**
	 * 资源路径
	 * @return
	 */
	public static String getBaseResourceDir() {
		return config.getString("loginServer.baseResourceDir");
	}
	/**
	 * 是否加密
	 * @return
	 */
	public static boolean isXorLoad() {
		return config.getBoolean("loginServer.isXorLoad");
	}
}
