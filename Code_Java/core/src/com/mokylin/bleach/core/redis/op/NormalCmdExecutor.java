package com.mokylin.bleach.core.redis.op;

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
 * 普通Redis指令的执行器，用于执行redis指令并得到应答。
 * 
 * @author pangchong
 *
 */
public class NormalCmdExecutor implements IRedisCmdExecutor, IRealExecuteRedisCommand<SingleRedisCmd<?>, IRedisResponse<?>> {
	
	final JedisPool jedisPool;
	
	final ISerializerPool serializerPool;
	
	final IActionOnCannotConnectRedis actionOnFail;
	
	NormalCmdExecutor(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail){
		this.jedisPool = jedisPool;
		this.serializerPool = serializerPool;
		this.actionOnFail = actionOnFail;
	}
	
	@SuppressWarnings("unchecked")
	public <R> IRedisResponse<R> execCommand(SingleRedisCmd<R> func){
		return (IRedisResponse<R>) ConnectionFailRetryStrategy.exec(this, func, actionOnFail);
	}
	
	public IRedisResponse<?> realExec(SingleRedisCmd<?> func){
		Jedis jedis = null;
		ISerializer serializer = null;
		try{
			serializer = serializerPool.borrow();
			jedis = jedisPool.getResource();
			Client jedisClient = jedis.getClient();
			if(jedisClient.isInMulti()){
				throw new RedisException("Cannot process operation when in transaction.");
			}
			func.apply(jedisClient, serializer);
			return RedisResponse.success(func.processResult(jedisClient.getOne(), jedisClient, serializer));
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
			if(serializer != null) serializerPool.returnResource(serializer);;
		}
	}

}
