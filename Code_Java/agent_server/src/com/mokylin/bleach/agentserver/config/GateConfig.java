package com.mokylin.bleach.agentserver.config;

import com.mokylin.bleach.core.config.model.NetInfo;

/**
 * Gate的配置
 * <p>2018-03-05 21:06
 *
 * @author Joey
 **/
public class GateConfig {

    /**监听客户端的IP端口信息*/
    private NetInfo netInfoToClient;

    /**监听Game的IP端口信息*/
    private NetInfo netInfoToGame;

    public NetInfo getNetInfoToClient() {
        return netInfoToClient;
    }

    public NetInfo getNetInfoToGame() {
        return netInfoToGame;
    }
}
