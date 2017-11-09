package com.mokylin.bleach.core.serializer;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * kryo序列化对象池
 * <P>
 * 使用此对象池来获取kryo对象，但是每个获取的对象最终都必须返回给该对象池
 * 
 * @author yaguang.xiao
 *
 */
public class KryoPool implements ISerializerPool{
	
	private GenericObjectPool<ISerializer> pool = null;
	
	public KryoPool(String[] packageNameList){
		pool = new GenericObjectPool<>(new KryoSerializerFactory(packageNameList));
		//TODO 下面这行代码设置在borrow对象的时候如果没有可用对象则抛出异常（默认情况是阻塞），是错误更早的暴露出来，上线之前此代码必须删掉
		pool.setBlockWhenExhausted(false);
		
		//TODO 下面这行代码设置对象池内的最大对象数量为无限，上线之前必须加上这行代码
//		pool.setMaxTotal(-1);
	}

	/**
	 * 初始化序列化对象池
	 * @param serializerCount 初始序列化对象的个数
	 * @throws Exception
	 */
	public void init(int serializerCount) throws Exception {
		if(serializerCount <= 0) {
			return;
		}
		
		//TODO 下面这行代码是设置对象池内最大对象数量，用在开发模式下，使错误更早的暴露出来，上线之前此代码必须删掉，并且要设置对象池内的最大对象数量为无限
		pool.setMaxTotal(serializerCount);
		
		
		for(int i = 1;i <= serializerCount;i ++) {
			pool.addObject();
		}
	}

	@Override
	public ISerializer borrow() {
		try {
			return pool.borrowObject();
		} catch (Exception e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public void returnResource(ISerializer borrowedSerializer) {
		if(borrowedSerializer == null) throw new IllegalArgumentException("Can not return a null to KryoPool.");
		pool.returnObject(borrowedSerializer);
	}

}
