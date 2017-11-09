package com.mokylin.bleach.core.config;

import java.util.Map;

import com.mokylin.bleach.core.config.model.RedisInfo;

/**
 * 各种映射关系，暂时通过配置来对应。等光神的中控服务器完毕 TODO
 * @author baoliang.shen
 *
 */
public class Mapping {
	
	/**无效的服务器ID*/
	public static final int INVALID_SERVER_ID = -1;
	
	/**<原始GameServerID,DataServerID>*/
	private Map<Integer,Integer> gs_dbs_map;
	
	/**<当前GameServerID,Redis名字>*/
	private Map<Integer, String> gs_redis_map;
	
	/**<Redis的名字，Redis的信息>*/
	private Map<String, RedisInfo> redis_info_map;
	
	/**
	 * <originalServerId,currentServerId>
	 * 原服务器ID对当前服务器ID的映射，合服后，多个原服务器ID会映射到同一个当前服务器ID
	 * */
	private Map<Integer, Integer> originalgs_currentgs_map;

	public Map<Integer,Integer> getGs_dbs_Map() {
		return gs_dbs_map;
	}

	public Map<Integer, String> getGs_redis_map() {
		return gs_redis_map;
	}

	public Map<String, RedisInfo> getRedis_info_map() {
		return redis_info_map;
	}

	public Map<Integer, Integer> getOriginalgs_currentgs_map() {
		return originalgs_currentgs_map;
	}

}
