package com.genesis.test.core.redisson;

import com.genesis.core.redis.redisson.RedisUtils;
import org.junit.Test;
import org.redisson.api.RBucket;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Key过期测试
 * <p>2018-03-02 11:30
 *
 * @author Joey
 **/
public class ExpireTest extends AbstractRedissonTest {

    @Test
    public void testExpire() throws InterruptedException {
        boolean bRet = false;
        int value = -1;

        RBucket<Integer> bucket = RedisUtils.getRBucket(redisson, "ExpireTest_testExpire");
        bucket.delete();

        bucket.trySet(99, 500, TimeUnit.MILLISECONDS);
        bRet = bucket.isExists();
        assertThat(bRet, is(true));

        // 一旦更新该Key的值，则过期时间会被移除
        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            bRet = bucket.isExists();
            System.out.println("是否存在：" + bRet);
        }

        final long remainTimeToLive = bucket.remainTimeToLive();
        System.out.println("该Key的剩余存活时间：" + remainTimeToLive);

        Thread.sleep(1000);
        bRet = bucket.isExists();
        assertThat(bRet, is(false));
    }
}
