package com.mokylin.bleach.core.redis.op;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import redis.clients.jedis.Client;
import redis.clients.jedis.exceptions.JedisDataException;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.serializer.ISerializer;

/**
 * 事务操作的基类。<p>
 * 
 * 在一个事务的内部会有多个操作产生结果，该基类封装了事务操作提交后对于各个操作结果的处理。<p>
 * 
 * 受限于Java对于内部和外部类的对象的传递，使用这种事后的方式来处理内部类的结果。
 * 
 * @author pangchong
 *
 */
public class AbstractTransactionOp {

	public AbstractTransactionOp() {
		super();
	}

	IRedisResponse<List<IRedisResponse<?>>> processExecutedResult(Queue<SingleRedisCmd<?>> allExecutedCmds, ISerializer serializer, Client client) {
		client.getAll(1);
		List<Object> results = client.getObjectMultiBulkReply();
		List<IRedisResponse<?>> allResponse = new ArrayList<>();
		if(results == null){
			return RedisResponse.success(allResponse);
		}
		
		for(Object each : results){
			SingleRedisCmd<?> cmd = allExecutedCmds.poll();
			if(!(each instanceof JedisDataException)){
				allResponse.add(RedisResponse.success(cmd.processResult(each, client, serializer)));
			}else{
				allResponse.add(RedisResponse.fail(((JedisDataException)each).fillInStackTrace().toString()));
			}				
		}
		return RedisResponse.success(allResponse);
	}

}