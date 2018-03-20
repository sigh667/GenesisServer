package com.genesis.core.redis;

import com.genesis.core.redis.config.RedisConfig;
import com.genesis.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;

import static com.google.common.base.Preconditions.checkNotNull;

public class RedisUtil {

    /**
     * 生产单一的Redis连接
     * @param action
     * @param config
     * @return
     */
    public static IRedis createRedis(IActionOnCannotConnectRedis action, RedisConfig config) {
        checkNotNull(config, "Can not add new redis connection with null redis config");
        return new RedisInstance(config, action);
    }
}
