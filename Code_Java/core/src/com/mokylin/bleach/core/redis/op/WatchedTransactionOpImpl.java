package com.mokylin.bleach.core.redis.op;

import java.util.List;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.exception.RedisException;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.ConnectionFailRetryStrategy;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.ISerializerPool;

/**
 * 对Key带有watch能力的事务实现，对应Redis的watch|multi|exec命令组。<p>
 * 
 * 注意：该实现对应的事务只能使用一次，即只能调用一次exec，之后就不能再次使用。但是在调用
 * exec之前，watch可以多次调用，以方便对多个key进行watch。
 * 
 * @author pangchong
 *
 */
public class WatchedTransactionOpImpl extends AbstractTransactionOp implements IWatchedTransactionOp, IRealExecuteRedisCommand<TransactionProcess, IRedisResponse<List<IRedisResponse<?>>>> {
	
	private JedisPool jedisPool = null;
	
	private ISerializerPool serializerPool = null;
	
	private Jedis jedis = null;
	
	private ISerializer serializer = null;
	
	private boolean isInTransaction = true;
	
	private final IActionOnCannotConnectRedis actionOnFail;

	WatchedTransactionOpImpl(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail) {
		this.jedisPool = jedisPool;
		this.serializerPool = serializerPool;
		isInTransaction = true;
		this.actionOnFail = actionOnFail;
	}

	@Override
	public IRedisResponse<List<IRedisResponse<?>>> exec(TransactionProcess func) {
		if(!isInTransaction) throw new RedisException("Can not execute redis transaction cmd in none transaction mode!");
		try {
			return ConnectionFailRetryStrategy.exec(this, func, actionOnFail);
		} finally {
			isInTransaction = false;
		}
	}
	
	public IRedisResponse<List<IRedisResponse<?>>> realExec(TransactionProcess func) {
		try{
			jedis = this.jedisPool.getResource();
			serializer = this.serializerPool.borrow();
			Client client = jedis.getClient();
			func.init(client, serializer);
			client.multi();
			func.apply();
			client.exec();
			return this.processExecutedResult(func.getCmdQueue(), serializer, client);
		}catch(Exception e){
			return RedisResponse.fail(e.fillInStackTrace().toString());
		}finally{
			if(jedis !=  null) {
				Client client = jedis.getClient();
				if(client.isBroken()) {
					this.jedisPool.returnBrokenResource(jedis);
				} else {
					this.jedisPool.returnResource(jedis);
				}
			}
			if(serializer != null) this.serializerPool.returnResource(serializer);
		}
	}
	
	@Override
	public IWatchedTransactionOp watch(String... keys) {
		if(keys == null || keys.length == 0) return this;
		jedis.watch(keys);
		return this;
	}

	@Override
	public IWatchedTransactionOp unwatch() {
		jedis.unwatch();
		return this;
	}

}
