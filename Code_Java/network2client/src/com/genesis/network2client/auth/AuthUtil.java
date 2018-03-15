package com.genesis.network2client.auth;

import com.genesis.redis.center.RedisLoginKey;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 登陆认证工具类
 * <p>2018-03-08 19:42
 *
 * @author Joey
 **/
public class AuthUtil {

    /**登陆锁存活秒数*/
    static final int anthLockExpireSec = 30;

    /**
     * 加锁
     * @param channel   渠道
     * @param accountId 账号
     * @return 是否成功
     */
    public static boolean lock(String channel, String accountId, RedissonClient redissonLogin) {
        final String key = RedisLoginKey.Lock.builderKey(channel, accountId);
        final RBucket<Boolean> bucket = redissonLogin.getBucket(key);
        // 不存在就创建一个
        if (!bucket.isExists()) {
            if (!bucket.trySet(false))
                return false;
        }

        // 查看是否锁住
        if (bucket.get())
            return false;

        // 尝试加锁
        return bucket.compareAndSet(false, true);
    }

    /**
     * 解锁
     * @param channel   渠道
     * @param accountId 账号
     * @return 是否成功
     */
    public static boolean unlock(String channel, String accountId, RedissonClient redissonLogin) {
        final String key = RedisLoginKey.Lock.builderKey(channel, accountId);
        final RBucket<Boolean> bucket = redissonLogin.getBucket(key);

        final boolean bRet = bucket.compareAndSet(true, false);
        bucket.expire(anthLockExpireSec, TimeUnit.SECONDS);// 设置锁的过期时间，节省内存
        return bRet;
    }
}
