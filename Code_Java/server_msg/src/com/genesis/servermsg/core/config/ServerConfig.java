package com.genesis.servermsg.core.config;

import com.genesis.servermsg.core.isc.ServerType;
import com.genesis.core.akka.config.AkkaConfig;

import static com.google.common.base.Preconditions.checkNotNull;

public class ServerConfig {

    /** 服务器类型 */
    public final ServerType serverType;
    /** 服务器的唯一标示Id */
    public final int serverId;
    /** IP、端口配置*/
    public final AkkaConfig akkaConfig;

    public ServerConfig(ServerType serverType, int serverId, AkkaConfig akkaConfig) {
        this.serverType = checkNotNull(serverType);
        this.serverId = serverId;
        this.akkaConfig = checkNotNull(akkaConfig);
    }
}
