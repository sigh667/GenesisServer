package com.test.redisson;

import com.genesis.core.redis.redisson.RedisUtils;
import org.junit.Test;
import org.redisson.api.RAtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * RAtomicLong测试
 * <p>2018-03-08 16:48
 *
 * @author Joey
 **/
public class RAtomicLongTest extends AbstractRedissonTest {
    @Test
    public void test() throws InterruptedException {
        boolean bRet = false;
        long value = -1;

        final RAtomicLong rAtomicLong = RedisUtils.getRAtomicLong(redisson, "RAtomicLongTest_test");
        rAtomicLong.delete();

        bRet = rAtomicLong.isExists();
        assertThat(bRet, is(false));

        value = rAtomicLong.incrementAndGet();
        assertThat(value, is(1L));
    }
}
