package com.genesis.logindbserver.globals;

import com.genesis.core.heartbeat.HeartbeatService;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局服务
 * <p>2018-03-17 15:16
 *
 * @author Joey
 **/
public class Globals {
    /**日志*/
    private static final Logger logger = LoggerFactory.getLogger(Globals.class);

    /**
     * 心跳服务
     */
    private static HeartbeatService heartBeatService = null;
    /**
     * Redis连接(运维中心Redis)
     */
    private static RedissonClient redisson;
}
