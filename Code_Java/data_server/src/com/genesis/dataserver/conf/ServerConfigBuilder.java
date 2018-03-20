package com.genesis.dataserver.conf;

import com.genesis.core.config.ConfigBuilder;
import com.typesafe.config.Config;

public class ServerConfigBuilder {

    private static String configName = "data_server.conf";
    private static Config conf = null;

    static {
        conf = ConfigBuilder.buildConfigFromFileName(configName);
    }

    public static Config config() {
        if (conf == null) {
            throw new RuntimeException("Can not find " + configName);
        }
        return conf;
    }
}
