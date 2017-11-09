package com.mokylin.bleach.core.redis.op;

import com.mokylin.bleach.core.redis.IRedisResponse;

/**
 * Redis中所有数据类型都可以使用的操作。
 * 
 * @author pangchong
 *
 */
public interface ICommonOp {
	
	/**
	 * 从Redis中删除指定的key。<p>
	 * 
	 * <b>注意：该方法会删除该key对应的所有数据，和key所存储的数据类型没有关系。</b>
	 * 
	 * 对应del key [key ...]。<p>
	 * 
	 * 时间复杂度：如果Key所对应的均为sds类型，则为O(n)，n为key的数量；如果某些key对应的为
	 * sds外的数据结构，则该key的时间复杂度为O(m)，m为该key对应的数据结构的元素个数。
	 * 
	 * @param key	要删除的key
	 * @param moreKeys	其他的要删除的key
	 * @return
	 */
	IRedisResponse<Long> del(String key, String... moreKeys);
}
