package com.mokylin.bleach.core.redis.op;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Client;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.ISerializerPool;

/**
 * Redis中Set数据结构操作的实现。
 * 
 * @author pangchong
 *
 */
public class SetOpImpl extends AbstractOp implements ISetOp {

	public SetOpImpl(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail) {
		super(jedisPool, serializerPool, actionOnFail);
	}

	public SetOpImpl(IRedisCmdExecutor cmdOp) {
		super(cmdOp);
	}

	@Override
	public IRedisResponse<Long> sadd(final String key, final Object... members) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				if(members == null || members.length == 0) throw new IllegalArgumentException("Can not sadd empty to a set!");
				if(members.length == 1){
					jedis.sadd(SafeEncoder.encode(key), serializer.serialize(members[0]));
					return;
				}
				
				byte[][] bMembers = new byte[members.length][];
				for(int i = 0; i < members.length; i++){
					bMembers[i] = serializer.serialize(members[i]);
				}
				jedis.sadd(SafeEncoder.encode(key), bMembers);
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> spop(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.spop(SafeEncoder.encode(key));
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				return serializer.deserialize((byte[])value, type);
			}
		});
	}

	@Override
	public <T> IRedisResponse<Set<T>> smembers(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<Set<T>>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.smembers(SafeEncoder.encode(key));
			}
			
			@Override
			public Set<T> processResult(Object value, Client jedis, ISerializer serializer){
				@SuppressWarnings("unchecked")
				List<byte[]> bSet = (List<byte[]>) value;
				if(bSet == null || bSet.isEmpty()) return null;
				Set<T> result = new HashSet<>();
				for(byte[] each : bSet){
					result.add(serializer.deserialize(each, type));
				}
				return result;
			}
		});
	}

	@Override
	public IRedisResponse<Boolean> sismember(final String key, final Object member) {
		return cmdOp.execCommand(new SingleRedisCmd<Boolean>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.sismember(SafeEncoder.encode(key), serializer.serialize(member));
			}
			
			@Override
			public Boolean processResult(Object value, Client jedis, ISerializer serializer){
				return ((Long)value) == 1;
			}
		});
	}

	@Override
	public IRedisResponse<Long> srem(final String key, final Object... members) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {

			@Override
			public void apply(Client jedis, ISerializer serializer) {
				if(members == null || members.length == 0) throw new IllegalArgumentException("Can not srem empty from a set!");
				if(members.length == 1){
					jedis.srem(SafeEncoder.encode(key), serializer.serialize(members[0]));
					return;
				}
				
				byte[][] bMembers = new byte[members.length][];
				for(int i = 0; i < members.length; i++){
					bMembers[i] = serializer.serialize(members[i]);
				}
				jedis.srem(SafeEncoder.encode(key), bMembers);				
			}
		});
	}

}
