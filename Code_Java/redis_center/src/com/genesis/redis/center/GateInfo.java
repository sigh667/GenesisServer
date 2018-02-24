package com.genesis.redis.center;

/**
 * Redis中Gate的实时信息
 * <p>2018-02-23 18:35
 *
 * @author Joey
 **/
public class GateInfo {
    public String ip2Client;    // 监听Client的IP
    public int port2Client;     // 监听Client的端口

    public String ip2Server;    // 监听Server的IP
    public int port2Server;     // 监听Server的端口

    public int currClientCount; // 当前Client数量
    public int maxClientCount;  // 最大Client数量
}
