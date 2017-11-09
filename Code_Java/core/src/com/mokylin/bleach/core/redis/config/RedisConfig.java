package com.mokylin.bleach.core.redis.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.mokylin.bleach.core.config.ConfigBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

/**
 * Redis的配置，用于指定具体的Redis的host，port和timeout。
 * 
 * @author pangchong
 *
 */
public class RedisConfig {

	/** Redis的服务器地址 */
	public final String host;

	/** Redis的服务器端口 */
	public final int port;

	/** 该Redis对应的Key值， 用于从不同的Redis连接中获取指定的Redis连接 */
	public final String serverKey;

	/** Jedis执行指令后等待的相应时间 */
	public int timeout = 0;

	/** 初始序列化对象数量 */
	public int initialSerializerObjectCount = 1;

	public RedisConfig(String serverKey, String host, int port, int initSerializerObjectCount){
		this.host = host;
		this.port = port;
		this.serverKey = serverKey;
		this.initialSerializerObjectCount = initSerializerObjectCount;
	}

	public RedisConfig(String serverKey, String host, int port, int initialSerializerObjectCount, int timeout){
		this.host = host;
		this.port = port;
		this.serverKey = serverKey;
		this.initialSerializerObjectCount = initialSerializerObjectCount;
		this.timeout = timeout;
	}

	/**
	 * 获取该config的一份相同数据、不同对象的copy。
	 * 
	 * @return
	 */
	public RedisConfig copy() {
		return new RedisConfig(serverKey, host, port, initialSerializerObjectCount, timeout);
	}

	private static Config config = ConfigBuilder.buildConfigFromFileName("redis.conf");

	/**
	 * 针对单个Redis配置的读取方法
	 * @return
	 */
	public static RedisConfig getRedisConfig() {
		Config tempConfig = config.getConfig("redis");
		String tempHost = tempConfig.getString("host");
		int tempPort = tempConfig.getInt("port");
		int count = tempConfig.getInt("kryoCount");
		// 因为只有一个Redis，所以serverKey就不重要了
		return new RedisConfig("", tempHost, tempPort, count);
	}

	/**
	 * 针对多个Redis配置的读取方法
	 * @return
	 */
	public static HashMap<String, RedisConfig> getRedisConfigs(){
		Collection<Entry<String, ConfigValue>> redisList = config.getConfig("redis").root().entrySet();
		if(redisList == null || redisList.isEmpty()) return new HashMap<>(0);

		HashMap<String, RedisConfig> redisMap = new HashMap<>(redisList.size());
		for(Entry<String, ConfigValue> each : redisList){
			String host = (String)((ConfigObject)each.getValue()).get("host").unwrapped();
			int port = (int)((ConfigObject)each.getValue()).get("port").unwrapped();
			ConfigValue initSerObjectCount = ((ConfigObject)each.getValue()).get("kryoCount");
			int initSerializerObjectCount = initSerObjectCount != null ? (int) initSerObjectCount.unwrapped() : 1;
			redisMap.put(each.getKey(), new RedisConfig(each.getKey(), host, port, initSerializerObjectCount));
		}
		return redisMap;
	}
}
