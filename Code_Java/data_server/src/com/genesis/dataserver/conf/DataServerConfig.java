package com.genesis.dataserver.conf;

import com.genesis.core.akka.config.AkkaConfig;
import com.genesis.core.config.ConfigBuilder;
import com.genesis.core.config.Mapping;
import com.genesis.core.config.ServerConfig;
import com.genesis.core.isc.ServerType;
import com.typesafe.config.Config;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DataServerConfig {

    private static Config config = ConfigBuilder.buildConfigFromFileName("DataServer.conf");
    public final ServerConfig serverConfig;
    public final SqlProperties sqlProperties;
    public final Mapping mappingConf;

    public DataServerConfig(ServerConfig sConfig, SqlProperties sqlProperties,
            Mapping mappingConf) {
        checkArgument(sqlProperties != null && sqlProperties.check());
        this.serverConfig = checkNotNull(sConfig);
        this.sqlProperties = sqlProperties;
        this.mappingConf = checkNotNull(mappingConf);
    }

    public static AkkaConfig getAkkaConfig() {
        return new AkkaConfig(config.getConfig("dataServer.akka"));
    }

    public static DataServerConfig getDataServerConfig() {
        return new DataServerConfig(
                new ServerConfig(ServerType.DB, config.getInt("dataServer.serverId"),
                        getAkkaConfig()), getSqlProperties(), getMappingConf());
    }

    private static Mapping getMappingConf() {
        return ConfigBuilder.buildConfigFromFileName("Mapping.conf", Mapping.class);
    }

    private static SqlProperties getSqlProperties() {
        Config subConfig = config.getConfig("dataServer.database");
        SqlProperties sqlProperties =
                new SqlProperties(subConfig.getString("ip"), subConfig.getInt("port"),
                        subConfig.getString("databaseName"), subConfig.getString("username"),
                        subConfig.getString("password"));
        return sqlProperties;
    }
}
