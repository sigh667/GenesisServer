package com.mokylin.bleach.core.redis.op;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * 对于Redis中List结构类型操作的封装接口。<p>
 * 
 * @author pangchong
 *
 */
public interface IListOp extends ICommonOp {
	
	/**
	 * 将指定的value加入到key对应的List的头部。<p>
	 * 
	 * 对应lpush key value [value ...]。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	IRedisResponse<Long> lpush(String key, Object... value);
	
	/**
	 * 只有当key存在且是一个List时，将value加入到List的头部。<p>
	 * 
	 * 对应lpushx key value。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	IRedisResponse<Long> lpushx(String key, Object value);
	
	/**
	 * 移除并返回key对应的List的头部的元素。<p>
	 * 
	 * 对应lpop key。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @return
	 */
	<T> IRedisResponse<T> lpop(String key, Class<T> type);
	
	/**
	 * 将指定的value加入到key对应的List的尾部。<p>
	 * 
	 * 对应rpush key value [value ...]。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	IRedisResponse<Long> rpush(String key, Object... value);
	
	/**
	 * 只有当key存在且是一个List时，将value加入到List的尾部。<p>
	 * 
	 * 对应rpushx key value。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	IRedisResponse<Long> rpushx(String key, Object value);
	
	/**
	 * 移除并返回key对应的List的尾部的元素。<p>
	 * 
	 * 对应rpop key。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @return
	 */
	<T> IRedisResponse<T> rpop(String key, Class<T> type);
	
	/**
	 * 移除并返回key对应的List的头部的元素，当List内没有元素时，该方法会阻塞。<p>
	 * 
	 * 对应blpop key 0。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @return
	 */
	<T> IRedisResponse<T> blpop(String key, Class<T> type);
	
	/**
	 * 移除并返回key对应的List的头部的元素，当List内没有元素时，该方法会阻塞，直到timeout超时。<p>
	 * 
	 * 对应blpop key timeout。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param timeout
	 * @return
	 */
	<T> IRedisResponse<T> blpop(String key, Class<T> type, int timeout);
	
	/**
	 * 移除并返回key对应的List的尾部的元素，当List内没有元素时，该方法会阻塞。<p>
	 * 
	 * 对应brpop key 0。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @return
	 */
	<T> IRedisResponse<T> brpop(String key, Class<T> type);
	
	/**
	 * 移除并返回key对应的List的尾部的元素，当List内没有元素时，该方法会阻塞，直到timeout超时。<p>
	 * 
	 * 对应brpop key timeout。<p>
	 * 
	 * 时间复杂度：O(1)
	 * 
	 * @param key
	 * @param timeout
	 * @return
	 */
	<T> IRedisResponse<T> brpop(String key, Class<T> type, int timeout);
}
