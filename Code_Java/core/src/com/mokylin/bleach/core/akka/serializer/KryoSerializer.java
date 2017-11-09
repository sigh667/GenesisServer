package com.mokylin.bleach.core.akka.serializer;

import akka.serialization.JSerializer;

import com.mokylin.bleach.core.serializer.ISerializer;
import com.mokylin.bleach.core.serializer.KryoPool;

public class KryoSerializer extends JSerializer {

	private static final int KRYO_OBJECT_NUM = 2;
	private static final KryoPool pool = new KryoPool(new String[] { "com.mokylin.bleach.core", "com.mokylin.bleach.servermsg" });
	static {
		try {
			pool.init(KRYO_OBJECT_NUM);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int identifier() {
		return 101;
	}

	@Override
	public boolean includeManifest() {
		return true;
	}

	@Override
	public byte[] toBinary(Object obj) {
		ISerializer serializer = pool.borrow();
		byte[] bytes = serializer.serialize(obj);
		pool.returnResource(serializer);
		return bytes;
	}

	@Override
	public Object fromBinaryJava(byte[] bytes, Class<?> clazz) {
		ISerializer serializer = pool.borrow();
		Object obj = serializer.deserialize(bytes, clazz);
		pool.returnResource(serializer);
		return obj;
	}

}
