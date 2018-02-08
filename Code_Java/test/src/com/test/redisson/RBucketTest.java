package com.test.redisson;

import com.mokylin.bleach.core.redis.redisson.RedisUtils;
import org.junit.Test;
import org.redisson.api.RBucket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * RBucket 映射为 redis server 的 string 类型
 * 只能存放最后存储的一个字符串
 * redis server 命令:
 * 查看所有键---->keys *
 * 查看key的类型--->type testBucket
 * 查看key的值 ---->get testBucket
 */
public class RBucketTest extends AbstractRedissonTest {

    /**
     * RBucket<String> 的相关测试
     */
    @Test
    public void testRBucketString() {
        RBucket<String> rBucket = RedisUtils.getInstance().getRBucket(redisson, "testBucket");

        //同步放置
        rBucket.set("redisBucketASync");
        String value = rBucket.get();
        assertThat(value, is("redisBucketASync"));

        //异步放置
        rBucket.setAsync("测试");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String bucketString = rBucket.get();
        assertThat(bucketString, is("测试"));
    }

    /**
     * RBucket<Integer> 的相关测试
     */
    @Test
    public void testRBucketInt() {
        RBucket<Integer> bucket = RedisUtils.getInstance().getRBucket(redisson, "testBucketInt");
        bucket.set(20);
        boolean bRet = bucket.compareAndSet(15, 25);
        assertThat(bRet, is(false));

        Integer value = bucket.get();
        assertThat(value, is(20));

        bRet = bucket.compareAndSet(20, 25);
        assertThat(bRet, is(true));
        value = bucket.get();
        assertThat(value, is(25));

        bRet = bucket.trySet(100);
        assertThat(bRet, is(false));

        RBucket<Integer> bucket2 = RedisUtils.getInstance().getRBucket(redisson, "testBucketInt2");
        bRet = bucket2.trySet(100);
        assertThat(bRet, is(true));

        value = bucket2.get();
        assertThat(value, is(100));

        Integer value2 = bucket2.getAndDelete();
        assertThat(value2, is(100));

        bRet = bucket2.isExists();
        assertThat(bRet, is(false));
    }
}
