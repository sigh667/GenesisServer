package com.genesis.redis.center.data;

import com.mokylin.bleach.core.config.model.NetInfo;

/**
 * Redis中Gate的实时信息
 * <p>2018-02-23 18:35
 *
 * @author Joey
 **/
public class GateInfo {

    public int id;  // 服务器ID

    /**监听客户端的IP端口信息*/
    public NetInfo netInfoToClient;

    /**监听Game的IP端口信息*/
    public NetInfo netInfoToGame;

    public int currClientCount; // 当前Client数量
    public int maxClientCount;  // 最大Client数量
}
