package com.mokylin.bleach.core.redis.op;

import java.util.List;

import redis.clients.jedis.Client;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.ISerializerPool;

public class ListOpImpl extends AbstractOp implements IListOp {

	public ListOpImpl(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail) {
		super(jedisPool, serializerPool, actionOnFail);
	}

	public ListOpImpl(IRedisCmdExecutor cmdOp) {
		super(cmdOp);
	}

	@Override
	public IRedisResponse<Long> lpush(final String key, final Object... value) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				if(value == null || value.length == 0) throw new IllegalArgumentException("Can not lpush empty to List!");
				if(value.length == 1){
					jedis.lpush(SafeEncoder.encode(key), new byte[][]{serializer.serialize(value[0])});
					return;
				}
				byte[][] bValues = new byte[value.length][];
				for(int i = 0; i < value.length; i++){
					bValues[i] = serializer.serialize(value[i]);
				}
				jedis.lpush(SafeEncoder.encode(key), bValues);
			}
		});
	}

	@Override
	public IRedisResponse<Long> lpushx(final String key, final Object value) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.lpushx(SafeEncoder.encode(key), new byte[][]{serializer.serialize(value)});
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> lpop(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.lpop(SafeEncoder.encode(key));
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				return serializer.deserialize((byte[])value, type);
			}
		});
	}

	@Override
	public IRedisResponse<Long> rpush(final String key, final Object... value) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				if(value == null || value.length == 0) throw new IllegalArgumentException("Can not lpush empty to List!");
				if(value.length == 1){
					jedis.rpush(SafeEncoder.encode(key), new byte[][]{serializer.serialize(value[0])});
					return;
				}
				byte[][] bValues = new byte[value.length][];
				for(int i = 0; i < value.length; i++){
					bValues[i] = serializer.serialize(value[i]);
				}
				jedis.rpush(SafeEncoder.encode(key), bValues);
			}
		});
	}

	@Override
	public IRedisResponse<Long> rpushx(final String key, final Object value) {
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.rpushx(SafeEncoder.encode(key), new byte[][]{serializer.serialize(value)});
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> rpop(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				jedis.rpop(SafeEncoder.encode(key));
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				return serializer.deserialize((byte[])value, type);
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> blpop(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				byte[][] args = new byte[1][];
				args[0] = SafeEncoder.encode(key);
				jedis.blpop(0, args);
				jedis.setTimeoutInfinite();
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				jedis.rollbackTimeout();
				@SuppressWarnings("unchecked")
				List<byte[]> result = (List<byte[]>) value;
				if(result == null || result.isEmpty()) return null;
				return serializer.deserialize(result.get(1), type);
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> blpop(final String key, final Class<T> type, final int timeout) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				byte[][] args = new byte[1][];
				args[0] = SafeEncoder.encode(key);
				jedis.blpop(timeout, args);
				jedis.setTimeoutInfinite();
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				jedis.rollbackTimeout();
				@SuppressWarnings("unchecked")
				List<byte[]> result = (List<byte[]>) value;
				if(result == null || result.isEmpty()) return null;
				return serializer.deserialize(result.get(1), type);
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> brpop(final String key, final Class<T> type) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				byte[][] args = new byte[1][];
				args[0] = SafeEncoder.encode(key);
				jedis.brpop(0, args);
				jedis.setTimeoutInfinite();
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				jedis.rollbackTimeout();
				@SuppressWarnings("unchecked")
				List<byte[]> result = (List<byte[]>) value;
				if(result == null || result.isEmpty()) return null;
				return serializer.deserialize(result.get(1), type);
			}
		});
	}

	@Override
	public <T> IRedisResponse<T> brpop(final String key, final Class<T> type, final int timeout) {
		return cmdOp.execCommand(new SingleRedisCmd<T>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				byte[][] args = new byte[1][];
				args[0] = SafeEncoder.encode(key);
				jedis.brpop(timeout, args);
				jedis.setTimeoutInfinite();
			}
			
			@Override
			public T processResult(Object value, Client jedis, ISerializer serializer){
				jedis.rollbackTimeout();
				@SuppressWarnings("unchecked")
				List<byte[]> result = (List<byte[]>) value;
				if(result == null || result.isEmpty()) return null;
				return serializer.deserialize(result.get(1), type);
			}
		});
	}

}
