package com.mokylin.bleach.core.redis.op;

import org.apache.commons.lang3.ArrayUtils;

import redis.clients.jedis.Client;
import redis.clients.jedis.JedisPool;

import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;
import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.ISerializerPool;

/**
 * Redis操作的抽象类，redis的数据结构的操作全部继承此类。
 * 
 * @author pangchong
 *
 */
public abstract class AbstractOp implements ICommonOp{

	/** Redis命令操作的执行器 */
	IRedisCmdExecutor cmdOp = null;
	
	AbstractOp(JedisPool jedisPool, ISerializerPool serializerPool, IActionOnCannotConnectRedis actionOnFail){
		cmdOp = new NormalCmdExecutor(jedisPool, serializerPool, actionOnFail);
	}
	
	AbstractOp(IRedisCmdExecutor cmdOp){
		this.cmdOp = cmdOp;
	}
	
	
	public IRedisResponse<Long> del(final String key, final String... moreKeys){
		return cmdOp.execCommand(new SingleRedisCmd<Long>() {
			@Override
			public void apply(Client jedis, ISerializer serializer) {
				if(moreKeys == null || moreKeys.length == 0){
					jedis.del(key);
				}
				jedis.del(ArrayUtils.add(moreKeys, key));
			}
		});
	}
}
