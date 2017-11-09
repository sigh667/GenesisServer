package com.mokylin.bleach.core.serializer;

/**
 * 序列化器
 *
 */
public interface ISerializer{

	/**
	 * 序列化
	 * @param o	需要被序列化的对象
	 * @return
	 */
	byte[] serialize(Object o);
	
	/**
	 * 反序列化
	 * @param b	需要被反序列化的字节数组
	 * @param type	反序列化出来的对象类型
	 * @return
	 */
	<T> T deserialize(byte[] b, Class<T> type);
}
