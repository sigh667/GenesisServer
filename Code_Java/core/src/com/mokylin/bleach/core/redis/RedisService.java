package com.mokylin.bleach.core.redis;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;
import com.mokylin.bleach.core.redis.config.RedisConfig;
import com.mokylin.bleach.core.redis.exception.RedisException;
import com.mokylin.bleach.core.redis.op.actiononconnectfail.IActionOnCannotConnectRedis;

public class RedisService {

	private Map<String, IRedis> map = new HashMap<>();

	public RedisService(IActionOnCannotConnectRedis action, RedisConfig... configs){
		if(configs == null || configs.length == 0) return;
		for(RedisConfig each : configs){
			if(!addNewRedisConnection(each, action)){
				throw new RedisException("Redis configuration error: same key [" + each.serverKey + "] found!");
			}
		}
	}

	/**
	 * 增加一个新的Redis连接。<p>
	 * 
	 * 如果新添加的Redis配置所对应的Redis Key已经存在，则返回false并什么都不做。
	 * 
	 * @param config
	 * @return
	 * @throws NullPointerException 当新的Redis配置为空时
	 */
	public boolean addNewRedisConnection(RedisConfig config, IActionOnCannotConnectRedis action){
		checkNotNull(config, "Can not add new redis connection with null redis config");

		if(map.containsKey(config.serverKey)) return false;

		map.put(config.serverKey, new RedisInstance(config, action));
		return true;
	}

	public Optional<IRedis> getRedis(String serverKey) {
		return Optional.fromNullable(map.get(serverKey));
	}
}
