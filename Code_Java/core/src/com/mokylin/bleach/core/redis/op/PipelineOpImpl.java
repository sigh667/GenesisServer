package com.mokylin.bleach.core.redis.op;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.ConnectionFailRetryStrategy;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.ISerializerPool;

/**
 * 用于处理pipeline操作的实现。
 * 
 * @author pangchong
 *
 */
public class PipelineOpImpl implements IPipelineOp, IRealExecuteRedisCommand<PipelineProcess, IRedisResponse<List<IRedisResponse<?>>>> {

	private final JedisPool jedisPool;
	
	private final ISerializerPool serializerPool;
	
	private final IActionOnCannotConnectRedis actionOnFail;
	
	public PipelineOpImpl(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail){
		this.jedisPool = jedisPool;
		this.serializerPool = serializerPool;
		this.actionOnFail = actionOnFail;
	}
	
	@Override
	public IRedisResponse<List<IRedisResponse<?>>> exec(PipelineProcess func) {
		return ConnectionFailRetryStrategy.exec(this, func, actionOnFail);
	}
	
	@Override
	public IRedisResponse<List<IRedisResponse<?>>> realExec(PipelineProcess func) {
		Jedis jedis = null;
		ISerializer serializer = null;
		try{
			serializer = serializerPool.borrow();
			jedis = jedisPool.getResource();
			Client client = jedis.getClient();
			func.init(client, serializer);
			func.apply();
			Queue<SingleRedisCmd<?>> allExecutedCmds = func.getCmdQueue();
			List<Object> results = client.getAll();
			List<IRedisResponse<?>> allResponse = new ArrayList<>();
			for(Object each : results){
				SingleRedisCmd<?> cmd = allExecutedCmds.poll();
				if(!(each instanceof JedisDataException)){
					allResponse.add(RedisResponse.success(cmd.processResult(each, client, serializer)));
				}else{
					allResponse.add(RedisResponse.fail(((JedisDataException)each).fillInStackTrace().toString()));
				}				
			}
			return RedisResponse.success(allResponse);
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

}
