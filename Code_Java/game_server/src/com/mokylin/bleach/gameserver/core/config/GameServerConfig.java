package com.mokylin.bleach.gameserver.core.config;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mokylin.bleach.core.akka.config.AkkaConfig;
import com.mokylin.bleach.core.config.ConfigBuilder;
import com.mokylin.bleach.core.config.Mapping;
import com.mokylin.bleach.core.config.ServerConfig;
import com.mokylin.bleach.core.isc.ServerType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

/**
 * 一个逻辑游戏服务器配置信息。
 * <p>
 * 
 * 初次获取服务器配置的使用方法：<br>
 * {@code List<GameServerConfig> gameConfigs = GameServerConfig.getGameServerConfig();}
 * <br>
 * 在初次获取之后，建议将获取到的配置保存下来以备后续使用。
 * 
 * @author pangchong
 * 
 */
public class GameServerConfig {

	/** 服务器的基础配置，包括ServerType，id，akka的host和port */
	public final ServerConfig serverConfig;

	/** GameServer所连接的的AgentServer信息 */
	public final ServerConfig agentServerConfig;

	/** 该服务器组，即原先所表示的渠道 */
	public final int group;

	/** 连接的Redis */
	public final String redis;

	/** 默认是否开放登录 */
	public final boolean isDefaultOpen;

	/** 逻辑游戏服务器所连接的DataServer ID */
	public final int[] dataServerIds;

	/** 是否允许本地登录，即不经过平台验证 */
	public final boolean isLocalLoginAllowed;
	
	public GameServerConfig(ServerConfig sConfig, int group, String redis,
			ServerConfig agentServerConfig, int[] dataServerIds,
			boolean isOpen, boolean isLocalLoginAllowed) {
		checkArgument(redis != null && !redis.isEmpty());
		checkArgument(dataServerIds != null && dataServerIds.length > 0);
		this.serverConfig = checkNotNull(sConfig);
		this.group = group;
		this.redis = checkNotNull(redis);
		this.agentServerConfig = checkNotNull(agentServerConfig);
		this.isDefaultOpen = isOpen;
		this.dataServerIds = dataServerIds;
		this.isLocalLoginAllowed = isLocalLoginAllowed;
	}

	/**
	 * 获取服务器类型。
	 * 
	 * @return
	 */
	public ServerType getServerType() {
		return this.serverConfig.serverType;
	}

	/**
	 * 获取服务器ID。
	 * 
	 * @return
	 */
	public int getServerId() {
		return this.serverConfig.serverId;
	}

	/**
	 * 获取连接的AgentServer的ID。
	 * 
	 * @return
	 */
	public int getConnectedAgentServerId() {
		return this.agentServerConfig.serverId;
	}

	/** 读入的GameServer.conf的配置的Config对象。 */
	private static Config config = ConfigBuilder
			.buildConfigFromFileName("GameServer.conf");

	/**
	 * 获取Mapping.conf的配置Mapping对象。
	 * 
	 * @return
	 */
	public static Mapping getServerMappings() {
		return ConfigBuilder.buildConfigFromFileName("Mapping.conf",
				Mapping.class);
	}

	/**
	 * 获取GameServer的Akka信息，包括host和port。
	 * 
	 * @return
	 */
	public static AkkaConfig getAkkaConfig() {
		return new AkkaConfig(config.getConfig("server.akka"));
	}

	public static boolean isXorLoad() {
		return config.getBoolean("server.isXorLoad");
	}
	/**
	 * 资源路径
	 * 
	 * @return
	 */
	public static String getBaseResourceDir() {
		return config.getString("server.baseResourceDir");
	}

	/**
	 * 资源是否混淆
	 * 
	 * @return
	 */
	public static boolean getResourceXorLoad() {
		return config.getBoolean("server.isXorLoad");
	}

	public static URL getTemplatesURL() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResource("templates.xml");
	}
	
	/**
	 * 获取日志相关的配置信息
	 * @return
	 */
	public static LogConfig getLogConfig() {
		Config logConfig = config.getConfig("server.logConfig");
		boolean mockSendLog = false;
		if(logConfig.hasPath("mockSendLog")) {
			mockSendLog = logConfig.getBoolean("mockSendLog");
		}
		return new LogConfig(logConfig.getString("scribeHost"),
				logConfig.getInt("scribePort"),
				logConfig.getLong("flushTimeGap"),
				logConfig.getLong("cacheMaxNum"),
				logConfig.getLong("flushTimeAccuracy"),
				mockSendLog);
	}

	/**
	 * 获取连接的AgentServer的配置信息，包括AgentServer的ID，host以及port。
	 * 
	 * @return
	 */
	public static ServerConfig getConnectedAgentConfig() {
		Config agentConfig = config.getConfig("server.connectTo.agentServer");
		return new ServerConfig(ServerType.AGENT_SERVER,
				agentConfig.getInt("id"), new AkkaConfig(
						agentConfig.getString("host"),
						agentConfig.getInt("port")));
	}

	/**
	 * 获取GameServer连接的全部DataServer的信息。
	 * 
	 * @return
	 */
	public static List<ServerConfig> getConnectedDataConfig() {
		Collection<ConfigValue> dataServerList = config
				.getConfig("server.connectTo.dataServers").root().values();
		if (dataServerList == null || dataServerList.isEmpty())
			return new ArrayList<>(0);

		List<ServerConfig> dsConfigs = new ArrayList<>();
		for (ConfigValue each : dataServerList) {
			ConfigObject co = (ConfigObject) each;
			dsConfigs.add(new ServerConfig(ServerType.DATA_SERVER, (int) co
					.get("id").unwrapped(), new AkkaConfig((String) co.get(
					"host").unwrapped(), (int) co.get("port").unwrapped())));
		}
		return dsConfigs;
	}

	/**
	 * 获取GameServer配置的全部信息。
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<GameServerConfig> getGameServerConfig() {
		Collection<ConfigValue> gameServerList = config
				.getConfig("server.gameServers").root().values();
		if (gameServerList == null || gameServerList.isEmpty())
			return new ArrayList<>(0);

		AkkaConfig akkaConfig = getAkkaConfig();
		List<ServerConfig> dataConfigs = getConnectedDataConfig();
		ServerConfig agentConfig = getConnectedAgentConfig();
		List<GameServerConfig> sConfigs = new ArrayList<>(gameServerList.size());
		for (ConfigValue each : gameServerList) {
			boolean isDefaultOpen = false;
			boolean isLocalLoginAllowed = false;
			ConfigObject co = (ConfigObject) each;
			if (co.get("open") != null) {
				isDefaultOpen = (boolean) co.get("open").unwrapped();
			}
			if (co.get("localLogin") != null) {
				isLocalLoginAllowed = (boolean) co.get("localLogin")
						.unwrapped();
			}
			int gameServerId = (int) co.get("id").unwrapped();
			List<Integer> dataServerIds = (List<Integer>) co.get("dataServers")
					.unwrapped();
			assertDataServerConfigured(dataServerIds, gameServerId, dataConfigs);
			sConfigs.add(new GameServerConfig(new ServerConfig(
					ServerType.GAME_SERVER, gameServerId, akkaConfig), (int) co
					.get("group").unwrapped(), (String) co.get("redis")
					.unwrapped(), agentConfig,
					convertToIntArray(dataServerIds), isDefaultOpen,
					isLocalLoginAllowed));
		}
		return sConfigs;
	}

	private static int[] convertToIntArray(List<Integer> dataServerIds) {
		int[] ret = new int[dataServerIds.size()];
		for (int i = 0; i < dataServerIds.size(); i++) {
			ret[i] = dataServerIds.get(i);
		}
		return ret;
	}

	private static void assertDataServerConfigured(List<Integer> dataServerIds,
			int gameServerId, List<ServerConfig> dataConfigs) {
		if (dataServerIds == null || dataServerIds.isEmpty()) {
			throw new RuntimeException("GameServer: [" + gameServerId
					+ "] can not started without data server!");
		}
		for (int eachId : dataServerIds) {
			boolean bFlag = false;
			for (ServerConfig eachConfig : dataConfigs) {
				if (eachConfig.serverId == eachId) {
					bFlag = true;
					continue;
				}
			}
			if (!bFlag) {
				throw new RuntimeException("DataServerID: [" + eachId
						+ "] configured in GameServer: [" + gameServerId
						+ "] has been not configured before!");
			}
		}
	}
}
