package com.genesis.test.core.redis;

import com.genesis.core.function.Function0;
import com.genesis.core.redis.IRedis;
import com.genesis.core.redis.RedisService;
import com.genesis.core.redis.config.RedisConfig;
import com.genesis.test.common.util.MultiThread;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.File;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AbstractOpTest {

    static RedisService redisService = new RedisService(null);

    static IRedis redis;

    static Process run;

    @BeforeClass
    public static void config() throws Exception {
        Future<Void> future = MultiThread.UTIL.asyncExecute(new Function0<Void>() {

            @Override
            public Void apply() {
                try {
                    File dir = new File("redis/redis-server.exe");
                    run = Runtime.getRuntime().exec(dir.getAbsolutePath());
                    run.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                return null;
            }
        });

        boolean redisStarted = false;
        try {
            future.get(5, TimeUnit.SECONDS);//等待redis启动
        } catch (TimeoutException e) {
            redisStarted = true;
        }

        if (!redisStarted) {
            Assert.fail("Test Redis can not started!");
            throw new RuntimeException("Test Redis can not started!");
        }
        redisService.addNewRedisConnection(new RedisConfig("default", "127.0.0.1", 6379, 3), null);
        redis = redisService.getRedis("default").get();
    }

    @AfterClass
    public static void bye() {
        run.destroy();
    }

    public MockEntity makeMockEntity() {
        MockEntity me = new MockEntity();
        me.setExp(13568);
        me.setId(133450909);
        me.setName("这是什么");
        me.setJapaneseName("くろさきいちご");
        return me;
    }
}
