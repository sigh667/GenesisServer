package com.genesis.gateserver.core.global;

import com.genesis.gateserver.config.GateConfig;
import com.genesis.gateserver.core.frontend.gameserver.GameServerFrontManager;
import com.mokylin.bleach.core.config.ConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Globals {

    /**日志*/
    private static final Logger logger = LoggerFactory.getLogger(Globals.class);

    /**本服配置*/
    private static GateConfig gateConfig;

    private static GameServerFrontManager gameServerManager = new GameServerFrontManager();

    public static void init() {
        // 1.0 读取配置
        gateConfig = ConfigBuilder.buildConfigFromFileName("GateServer.conf", GateConfig.class);
        logger.info("conf文件读取完毕");
    }


    public static GateConfig getGateConfig() { return gateConfig; }

    public static GameServerFrontManager getGameServerManager() {
        return gameServerManager;
    }
}
