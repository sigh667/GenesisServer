package com.mokylin.bleach.core.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.mokylin.bleach.core.redis.config.RedisConfig;
import com.mokylin.bleach.core.redis.op.HashOpImpl;
import com.mokylin.bleach.core.redis.op.IHashOp;
import com.mokylin.bleach.core.redis.op.IListOp;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.ISetOp;
import com.mokylin.bleach.core.redis.op.ITransactionOp;
import com.mokylin.bleach.core.redis.op.IValueOp;
import com.mokylin.bleach.core.redis.op.ListOpImpl;
import com.mokylin.bleach.core.redis.op.PipelineOpImpl;
import com.mokylin.bleach.core.redis.op.SetOpImpl;
import com.mokylin.bleach.core.redis.op.TransactionOpImpl;
import com.mokylin.bleach.core.redis.op.ValueOpImpl;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializerPool;
import com.mokylin.bleach.core.serializer.KryoPool;

final class RedisInstance implements IRedis{
	
	private RedisConfig config = null;
	
	private JedisPool jedisPool = null;
	
	private ISerializerPool serializerPool = null;
	
	private IValueOp valueOp = null;
	
	private IHashOp hashOp = null;
	
	private IListOp listOp = null;
	
	private ISetOp setOp = null;
	
	private final IActionOnCannotConnectRedis actionOnFail;
	
	/**
	 * 
	 * @param config
	 * @param actionOnFail Redis连接失败时的处理方法，正式上线时，应该不允许传null TODO
	 */
	RedisInstance(RedisConfig config, IActionOnCannotConnectRedis actionOnFail){
		this.config = config.copy();
		this.actionOnFail = actionOnFail;
		jedisPool = new JedisPool(new GenericObjectPoolConfig(), config.host, config.port, config.timeout);
		KryoPool kryoPool = new KryoPool(new String[] { "com.mokylin.bleach.gamedb" });
		try {
			kryoPool.init(config.initialSerializerObjectCount);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		serializerPool = kryoPool;
		valueOp = new ValueOpImpl(jedisPool, serializerPool, this.actionOnFail);
		hashOp = new HashOpImpl(jedisPool, serializerPool, this.actionOnFail);
		listOp = new ListOpImpl(jedisPool, serializerPool, this.actionOnFail);
		setOp = new SetOpImpl(jedisPool, serializerPool, this.actionOnFail);
	}
	
	public IValueOp getValueOp() {
		return valueOp;
	}
	
	public IHashOp getHashOp(){
		return hashOp;
	}
	
	public IListOp getListOp(){
		return listOp;
	}
	
	public ISetOp getSetOp(){
		return setOp;
	}
	
	public IPipelineOp pipeline(){
		return new PipelineOpImpl(this.jedisPool, this.serializerPool, this.actionOnFail);
	}
	
	public String getServerKey(){
		return this.config.serverKey;
	}
	
	public String toString(){
		return this.config.serverKey + ": " + this.config.host + ":" + this.config.port;
	}

	@Override
	public ITransactionOp multi() {
		return new TransactionOpImpl(jedisPool, serializerPool, this.actionOnFail);
	}

	@Override
	public boolean isConnected() {
		Jedis jedis = jedisPool.getResource();
		boolean isConnected = jedis.getClient().isConnected();
		jedisPool.returnResource(jedis);
		return isConnected;
	}

}
