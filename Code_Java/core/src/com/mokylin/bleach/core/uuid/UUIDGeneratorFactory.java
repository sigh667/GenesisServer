package com.mokylin.bleach.core.uuid;

import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.uuid.type.IUUIDType;

/**
 * UUID生成器工厂
 * 
 * @author yaguang.xiao
 *
 */

public class UUIDGeneratorFactory {
	
	/**
	 * 创建UUID生成器
	 * @param serverGroup	服务器组Id<font color='red'>（最大值是256，最小值是0）</font>
	 * @param serverId	服务器Id<font color='red'>（最大值是131071，最小值是0）</font>
	 * @param enumClass UUID枚举
	 * @param iRedis TODO
	 * @return	UUID生成器
	 */
	public static IUUIDGenerator createUUIDGenerator(int serverGroup, int serverId, Class<? extends IUUIDType> enumClass, IRedis iRedis) {
		return new UUIDGeneratorImpl(serverGroup, serverId, enumClass, iRedis);
	}
	
}
