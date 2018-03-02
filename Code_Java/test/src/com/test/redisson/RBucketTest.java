package com.test.redisson;

import com.mokylin.bleach.core.redis.redisson.RedisUtils;
import org.junit.Test;
import org.redisson.api.RBucket;

import java.util.concurrent.TimeUnit;

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
        RBucket<String> rBucket = RedisUtils.getRBucket(redisson, "testBucket");

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
        // 0
        RBucket<Integer> bucket = RedisUtils.getRBucket(redisson, "testBucketInt");
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

        // 2
        RBucket<Integer> bucket2 = RedisUtils.getRBucket(redisson, "testBucketInt2");
        bRet = bucket2.trySet(100);
        assertThat(bRet, is(true));

        value = bucket2.get();
        assertThat(value, is(100));

        Integer value2 = bucket2.getAndDelete();
        assertThat(value2, is(100));

        bRet = bucket2.isExists();
        assertThat(bRet, is(false));
    }

    @Test
    public void testCompareAndSet() {
        boolean bRet = false;
        int value = -1;

        RBucket<Integer> bucket1 = RedisUtils.getRBucket(redisson, "RBucketTest_testCompareAndSet");
        bucket1.delete();

        // 1.0 key不存在，compareAndSet 必失败
        bRet = bucket1.compareAndSet(1, 2);
        assertThat(bRet, is(false));
        //value = bucket1.get();    // 这里这样调用是会抛空指针异常

        // 2.0 key不存在，trySet必成功
        bRet = bucket1.trySet(99);
        assertThat(bRet, is(true));

        // 3.0 key不等于1，所以失败
        bRet = bucket1.compareAndSet(1, 2);
        assertThat(bRet, is(false));

        // 4.0 key等于99，所以成功
        bRet = bucket1.compareAndSet(99, 2);
        assertThat(bRet, is(true));
    }

    @Test
    public void testTrySet() {
        boolean bRet = false;
        int value = -1;

        RBucket<Integer> bucket = RedisUtils.getRBucket(redisson, "RBucketTest_testTrySet");
        bucket.delete();

        //key不存在，trySet必成功
        bRet = bucket.trySet(1);
        assertThat(bRet, is(true));

        //key存在，trySet必失败
        bRet = bucket.trySet(2);
        assertThat(bRet, is(false));

        //key存在，trySet必失败(哪怕是设置相同的值)
        bRet = bucket.trySet(1);
        assertThat(bRet, is(false));
    }
}
