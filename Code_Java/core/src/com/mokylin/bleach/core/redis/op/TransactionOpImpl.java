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
 * 用于处理transaction操作的实现。
 * 
 * @author pangchong
 *
 */
public class TransactionOpImpl extends AbstractTransactionOp implements ITransactionOp, IRealExecuteRedisCommand<TransactionProcess, IRedisResponse<List<IRedisResponse<?>>>> {
	
	final JedisPool jedisPool;
	
	final ISerializerPool serializerPool;
	
	final IActionOnCannotConnectRedis actionOnFail;
	
	public TransactionOpImpl(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail){
		this.jedisPool = jedisPool;
		this.serializerPool = serializerPool;
		this.actionOnFail = actionOnFail;
	}

	@Override
	public IRedisResponse<List<IRedisResponse<?>>> exec(TransactionProcess func) {
		return ConnectionFailRetryStrategy.exec(this, func, actionOnFail);
	}
	
	@Override
	public IRedisResponse<List<IRedisResponse<?>>> realExec(TransactionProcess func) {
		Jedis jedis = null;
		ISerializer serializer = null;
		try{
			serializer = serializerPool.borrow();
			jedis = jedisPool.getResource();
			Client client = jedis.getClient();
			client.multi();
			func.init(client, serializer);
			func.apply();
			client.exec();
			return processExecutedResult(func.getCmdQueue(), serializer, client);
		}catch(Exception e){
			return RedisResponse.fail(e.fillInStackTrace().toString());
		}finally{
			if(jedis != null) {
				Client client = jedis.getClient();
				if(client.isBroken()) {
					jedisPool.returnBrokenResource(jedis);
				} else {
					jedisPool.returnResource(jedis);
				}
			}
			if(serializer != null) serializerPool.returnResource(serializer);
		}
	}

	@Override
	public IWatchedTransactionOp watch(String... keys) {
		if(keys == null || keys.length == 0) throw new RedisException("Can not watch empty keys!");
		return new WatchedTransactionOpImpl(jedisPool, serializerPool, this.actionOnFail);
	}

}
