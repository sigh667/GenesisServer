package com.genesis.redis.center;

import java.util.HashSet;
import java.util.Set;

/**
 * 负责登陆的Redis中的key组装器
 * <p>2018-02-26 16:35
 *
 * @author Joey
 **/
public enum RedisLoginKey {
    /**加锁*/
    Lock("L"),
    /**准备登陆Gate*/
    ToGate("T"),
    /**已经在Gate中*/
    InGate("I"),
    /**账号信息*/
    AccountInfo(""),
    ;

    private static String separator = ":";  // 分隔符
    private static Set<String> checkSet = new HashSet<>();
    static {
        // 运行时，自动检测前缀是否重复
        for (RedisLoginKey e : RedisLoginKey.values()) {
            if (checkSet.contains(e.prefix)) {
                throw new RuntimeException("RedisLoginKey's prefix repeated! prefix==" + e.prefix);
            }
        }
    }

    private String prefix;

    RedisLoginKey(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 构建各种Key
     * @param channel   渠道
     * @param accountId 账号ID
     * @return
     */
    public String builderKey(String channel, String accountId) {
        return prefix + separator + channel + separator + accountId;
    }
}
