package com.test.redisson;

import com.mokylin.bleach.core.redis.redisson.RedisUtils;
import org.junit.After;
import org.junit.Before;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class AbstractRedissonTest {

    RedissonClient redisson;

    /**
     * 每次在测试方法运行之前 运行此方法
     * 创建客户端连接服务器的redisson对象
     */
    @Before
    public void before() {
        String ip = "127.0.0.1";
        String port = "6379";
        Config config = RedisUtils.buildConfig(ip, port);
        redisson = RedisUtils.getRedisson(config);
    }

    /**
     * 每次测试方法运行完之后 运行此方法
     * 用于关闭客户端连接服务器的redisson对象
     */
    @After
    public void after(){
        RedisUtils.closeRedisson(redisson);
    }
}
