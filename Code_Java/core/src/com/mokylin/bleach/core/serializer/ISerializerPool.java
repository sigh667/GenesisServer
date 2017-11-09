package com.mokylin.bleach.core.serializer;

/**
 * 序列化对象池
 *
 */
public interface ISerializerPool {

	/**
	 * 获取序列化对象
	 * @return
	 */
	ISerializer borrow();

	/**
	 * 返回序列化对象
	 * @param serializer
	 */
	void returnResource(ISerializer serializer);

}
