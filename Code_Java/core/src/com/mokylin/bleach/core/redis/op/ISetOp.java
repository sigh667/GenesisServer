package com.mokylin.bleach.core.redis.op;

import java.util.Set;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * 对于Redis中Set结构类型操作的封装接口。
 * 
 * @author pangchong
 *
 */
public interface ISetOp extends ICommonOp{
	
	/**
	 * 将多个值加入到key对应的set中，如果key类型不为set，则失败。<p>
	 * 
	 * 对应sadd key member [member ...]。<p>
	 * 
	 * 时间复杂度: O(n)，n是member的个数。
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	IRedisResponse<Long> sadd(String key, Object... members);
	
	/**
	 * 从Key对应的set中移除并返回随机一个value。<p>
	 * 
	 * 对应spop key。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	<T> IRedisResponse<T> spop(String key, Class<T> type);
	
	/**
	 * 返回key所对应的set的所有值。如果key不存在，则返回null。<p>
	 * 
	 * <b>注意：该接口将key所对应的set中所有的member视为同一种Java数据类型。</b>
	 * 
	 * 对应smembers key。<p>
	 * 
	 * 时间复杂度: O(n)，n是set中member的个数。
	 * 
	 * @param key
	 * @param type
	 * @return
	 */
	<T> IRedisResponse<Set<T>> smembers(String key, Class<T> type);
	
	/**
	 * 判断member是否是key指定的set中的元素。<p>
	 * 
	 * 对应sismember key member。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	IRedisResponse<Boolean> sismember(String key, Object member);
	
	/**
	 * 从Key对应的set中移除对应的member，如果Key不存在，则什么都不做；该命令会忽略不存在的member。返回成功删除的个数。<p>
	 * 
	 * <b>注意：如果不传入任何members的话，该方法会抛出异常！</b>
	 * 
	 * 对应 srem key member [member ...]。<p>
	 * 
	 * 时间复杂度：O(n)
	 * 
	 * @param key
	 * @param members
	 * @return
	 */
	IRedisResponse<Long> srem(String key, final Object... members);
}
