package com.mokylin.bleach.core.redis.op;

import redis.clients.jedis.Client;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.ISerializerPool;

/**
 * Redis中单值操作的实现。
 * 
 * @author pangchong
 *
 */
public class ValueOpImpl extends AbstractOp implements IValueOp {
	
	public ValueOpImpl(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail) {
		super(jedisPool, serializerPool, actionOnFail);
	}
	
	public ValueOpImpl(IRedisCmdExecutor cmdOp){
		super(cmdOp);
	}

	@Override
	public IRedisResponse<String> set(final String key, final Object value) {
		return cmdOp.execCommand(new SingleRedisCmd<String>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.set(SafeEncoder.encode(key), serializer.serialize(value));
			}
		});
	}

	@Override
	public IRedisResponse<String> setIfAbsent(final String key, final Object value) {
		return cmdOp.execCommand(new SingleRedisCmd<String>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.set(SafeEncoder.encode(key), serializer.serialize(value), SafeEncoder.encode("NX"));
			}
		});
	}

	@Override
	public IRedisResponse<String> setIfPresent(final String key, final Object value) {
		return cmdOp.execCommand(new SingleRedisCmd<String>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.set(SafeEncoder.encode(key), serializer.serialize(value), SafeEncoder.encode("XX"));
			}
		});
	}

	@Override
	public IRedisResponse<String> setExpiredBySeconds(final String key, final Object value, final long second) {
		return cmdOp.execCommand(new SingleRedisCmd<String>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.set(SafeEncoder.encode(key), serializer.serialize(value), SafeEncoder.encode("EX"), second);
			}
		});
	}

	@Override
	public IRedisResponse<String> setExpiredByMilliseconds(final String key, final Object value, final long millisenconds) {
		return cmdOp.execCommand(new SingleRedisCmd<String>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.set(SafeEncoder.encode(key), serializer.serialize(value), SafeEncoder.encode("PX"), millisenconds);
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> get(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.get(SafeEncoder.encode(key));				
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				return serializer.deserialize((byte[])value, type);
			}
		});		
	}

}
