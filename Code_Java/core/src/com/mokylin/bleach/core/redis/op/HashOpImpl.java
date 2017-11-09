package com.mokylin.bleach.core.redis.op;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import redis.clients.jedis.Client;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.ISerializerPool;

/**
 * 对于Redis中指定key所对应的某一个Hash结构类型操作的实现。<p>
 * 
 * @author pangchong
 *
 */
public class HashOpImpl extends AbstractOp implements IHashOp {

	public HashOpImpl(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail) {
		super(jedisPool, serializerPool, actionOnFail);
	}

	public HashOpImpl(IRedisCmdExecutor cmdOp) {
		super(cmdOp);
	}

	@Override
	public IRedisResponse<Long> hset(final String key, final String field, final Object value) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(field), serializer.serialize(value));
			}
		});
	}

	@Override
	public IRedisResponse<Long> hsetnx(final String key, final String field, final Object value) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.hsetnx(SafeEncoder.encode(key), SafeEncoder.encode(field), serializer.serialize(value));
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> hget(final String key, final String field, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				return serializer.deserialize((byte[])value, type);
			}
		});
	}

	@Override
	public IRedisResponse<String> hmset(final String key, final Map<String, ?> maps) {
		return cmdOp.execCommand(new SingleRedisCmd<String>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				if(maps == null || maps.size() == 0) throw new IllegalArgumentException("parameters of hmset can not be null!");
				Map<byte[], byte[]> bMaps = new HashMap<>();
				for(Entry<String, ?> each : maps.entrySet()){
					bMaps.put(SafeEncoder.encode(each.getKey()), serializer.serialize(each.getValue()));
				}
				jedis.hmset(SafeEncoder.encode(key), bMaps);
			}
		});
	}

	@Override
	public IRedisResponse<Long> hdel(final String key, final String field, final String... moreFields) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {

			@Override
			public void apply(Client jedis, ISerializer serializer) {
				if(moreFields == null || moreFields.length == 0){
					jedis.hdel(SafeEncoder.encode(key), SafeEncoder.encodeMany(field));
					return;
				}
				
				jedis.hdel(SafeEncoder.encode(key), SafeEncoder.encodeMany(ArrayUtils.add(moreFields, field)));
			}
		});
	}

	@Override
	public <T> IRedisResponse<Map<String, T>> hgetall(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<Map<String, T>>() {

			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.hgetAll(key);
			}

			@Override
			public Map<String, T> processResult(Object value, Client jedis, ISerializer serializer){
				@SuppressWarnings("unchecked")
				List<byte[]> binaryValues = (List<byte[]>) value;
				if(binaryValues == null || binaryValues.isEmpty()) return null;
				
				Iterator<byte[]> iterator = binaryValues.iterator();
				Map<String, T> valueMap = new HashMap<>();
				while(iterator.hasNext()){
					valueMap.put(SafeEncoder.encode(iterator.next()), serializer.deserialize(iterator.next(), type));
				}
				return valueMap;
			}
		});
	}
}
