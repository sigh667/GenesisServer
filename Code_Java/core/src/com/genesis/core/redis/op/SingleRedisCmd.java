package com.genesis.core.redis.op;

import com.genesis.core.function.Function2ReturnVoid;
import com.genesis.core.serializer.ISerializer;

import redis.clients.jedis.Client;
import redis.clients.util.SafeEncoder;

/**
 * 用于表示单独一个Redis指令的函数类。<p>
 *
 * @see IRedisCmdExecutor
 *
 * @author pangchong
 *
 * @param <R>
 */
public abstract class SingleRedisCmd<R> implements Function2ReturnVoid<Client, ISerializer> {

    @SuppressWarnings("unchecked")
    public R processResult(Object o, Client jedis, ISerializer serializer) {
        if (o instanceof byte[]) {
            return (R) SafeEncoder.encode((byte[]) o);
        }
        return (R) o;
    }
}
