package com.mokylin.bleach.core.redis.op;

import com.mokylin.bleach.core.redis.IRedisResponse;


/**
 * 对于Redis中sds结构（也称为单字段或者是string）类型操作的封装接口。<p>
 * 
 * <b>注意：除setIfAbsent方法外，该接口的全部setXXX方法会直接替换成现有的value。</b>
 * 
 * @author pangchong
 * 
 */
public interface IValueOp extends ICommonOp{

	/**
	 * 使用key将value放入redis，如果key存在则覆盖。<p>
	 * 
	 * 对应set key value。<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	IRedisResponse<String> set(String key, Object value);
	
	/**
	 * 使用key将value放入redis仅当key不存在时，如果key存在则无效。<p>
	 * 
	 * 对应set key value nx<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	IRedisResponse<String> setIfAbsent(String key, Object value);
	
	/**
	 * 使用key将value放入redis仅当key存在时，如果key不存在则无效。<p>
	 * 
	 * 对应set key value xx<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	IRedisResponse<String> setIfPresent(String key, Object value);
	
	/**
	 * 使用key将value放入redis，并且将该key设置为second秒后过期。<p>
	 * 
	 * 对应set key value ex seconds。<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param value
	 * @param second
	 * @return
	 */
	IRedisResponse<String> setExpiredBySeconds(String key, Object value, long second);
	
	/**
	 * 使用key将value放入redis，并且将该key设置为millisenconds毫秒后过期。<p>
	 * 
	 * 对应set key value px millisenconds。<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param value
	 * @param millisenconds
	 * @return
	 */
	IRedisResponse<String> setExpiredByMilliseconds(String key, Object value, long millisenconds);
	
	/**
	 * 根据key获取在Redis中对应的值。<b>如果该key所表示redis结构不为sds，则失败。</b><p>
	 * 
	 * 对应get key。<p>
	 * 
	 * 时间复杂度: O(1)
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	<T> IRedisResponse<T> get(String key, Class<T> type);
}
