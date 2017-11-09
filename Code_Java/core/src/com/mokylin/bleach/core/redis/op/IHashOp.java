package com.mokylin.bleach.core.redis.op;

import java.util.Map;

import com.mokylin.bleach.core.redis.IRedisResponse;


/**
 * 对于Redis中指定key所对应的某一个Hash结构类型操作的封装接口。<p>
 * 
 * @author pangchong
 *
 */
public interface IHashOp extends ICommonOp{
	
	/**
	 * 将value存入并映射到field上，并返回1，如果原key field存在，则覆盖，并且返回0。<p>
	 * 
	 * 对应hset key field value。<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	IRedisResponse<Long> hset(String key, String field, Object value);
	
	/**
	 * 当field不存在时，将value存入并映射到field上，并返回1；如果原key field存在，则什么都不做，返回0。<p>
	 * 
	 * 对应hsetnx key field value。<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	IRedisResponse<Long> hsetnx(String key, String field, Object value);
	
	/**
	 * 获取field所指定的值。如果不存在，则返回null。<p>
	 * 
	 * 对应hget key field。<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param field
	 * @param type
	 * @return
	 */
	<T> IRedisResponse<T> hget(String key, String field, Class<T> type);
	
	/**
	 * 获取key所指定的全部hash的内容。如果key不存在，则返回null。<p>
	 * 
	 * 对应hgetall key。<p>
	 * 
	 * 时间复杂度：O(n)，n是key所包含的hash的value的个数。
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	<T> IRedisResponse<Map<String, T>> hgetall(String key, Class<T> type);
	
	/**
	 * 将多个value存入并分别映射到对应的field上。<p>
	 * 
	 * 对应hmset key field value [field value ...]。<p>
	 * 
	 * 时间复杂度：O(n)，n为要设置的field数量。
	 * 
	 * @param key
	 * @param maps
	 * @return
	 */
	IRedisResponse<String> hmset(String key, Map<String, ?> maps);
	
	/**
	 * 删除指定的fields。<p>
	 * 
	 * 对应hdel key field [field ...]。<p>
	 * 
	 * 时间复杂度：O(n)，n为要删除的field数量。
	 * 
	 * @param key
	 * @param field
	 * @param moreFields
	 * @return
	 */
	IRedisResponse<Long> hdel(String key, String field, String... moreFields);
}
