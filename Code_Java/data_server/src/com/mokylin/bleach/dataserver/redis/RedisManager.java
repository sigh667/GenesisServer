package com.mokylin.bleach.dataserver.redis;

import com.mokylin.bleach.core.config.Mapping;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.RedisService;
import com.mokylin.bleach.core.redis.config.RedisConfig;
import com.mokylin.bleach.dataserver.globals.Globals;

/**
 * DataServer的Redis连接管理器，会访问多个Redis
 * @author baoliang.shen
 *
 */
public class RedisManager {

	private final RedisService redisService;

	public RedisManager(){
		redisService = new RedisService(null, RedisConfig.getRedisConfigs().values().toArray(new RedisConfig[0]));
	}

	/**
	 * 根据原服务器ID取IRedis
	 * @param originalServerId
	 * @return
	 */
	public IRedis getIRedis(Integer originalServerId) {
		Mapping mapConf = Globals.getServerConfig().mappingConf;
		Integer currentServerId = mapConf.getOriginalgs_currentgs_map().get(originalServerId);
		String redisName = mapConf.getGs_redis_map().get(currentServerId);
		
		return redisService.getRedis(redisName).get();
	}

}
