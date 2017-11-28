package com.mokylin.bleach.core.config;

import com.mokylin.bleach.core.akka.config.AkkaConfig;
import com.mokylin.bleach.core.isc.ServerType;

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
