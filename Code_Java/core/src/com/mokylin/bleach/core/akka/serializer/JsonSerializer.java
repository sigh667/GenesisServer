package com.mokylin.bleach.core.akka.serializer;

import akka.serialization.JSerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json序列化工具
 * 
 * @author yaguang.xiao
 *
 */

public class JsonSerializer extends JSerializer {
	
	/**
	 * Pick a unique identifier for your Serializer,
	 * you've got a couple of billions to choose from,
	 * 0 - 16 is reserved by Akka itself
	 */
	@Override
	public int identifier() {
		return 100;
	}

	/**
	 * This is whether "fromBinary" requires a "clazz" or not
	 */
	@Override
	public boolean includeManifest() {
		return true;
	}

	/**
	 * "toBinary" serializes the given object to an Array of Bytes
	 */
	@Override
	public byte[] toBinary(Object obj) {
		return JSON.toJSONBytes(obj, SerializerFeature.WriteClassName);
	}

	/**
	 * "fromBinary" deserializes the given array,
	 * using the type hint (if any, see "includeManifest" above)
	 */
	@Override
	public Object fromBinaryJava(byte[] bytes, Class<?> clazz) {
		return JSON.parseObject(bytes, clazz);
	}

}
