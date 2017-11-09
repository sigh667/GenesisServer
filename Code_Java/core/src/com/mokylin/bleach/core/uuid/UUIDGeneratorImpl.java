package com.mokylin.bleach.core.uuid;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.uuid.type.IUUIDType;

/**
 * UUID生成器的Id实现
 * 
 * <p>在这里为{@link #UUID_TYPE_PACKAGE}包中的每一个{@link IUUIDType}的实现创建一个对应的UUID64对象<br>
 * 由创建的UUID64对象生成它对应类型的新的UUID
 * 
 * <p>UUID的格式：服务器组id（6位）+服务器id（17位）+对象id（40位）
 * 
 * @author yaguang.xiao
 * 
 */

class UUIDGeneratorImpl implements IUUIDGenerator {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(UUIDGeneratorImpl.class);
	/** 服务器组Id位数 */
	static final int SERVERGROUP_BIT_NUM = 8;
	/** 服务器Id位数 */
	static final int SERVER_BIT_NUM = 17;
	/** 对象Id位数 */
	static final int OBJECT_ID_BIT_NUM = 38;
	/** 每次服务器启动跳过的Id数 */
	private static final int UUID_STEP = 100;
	/** 所有类型的UUID生成器 */
	private Map<IUUIDType, UUID64> uuids = Maps.newHashMap();

	/**
	 * 构建UUID生成器
	 * @param serverGroup
	 *            服务器组Id
	 * @param serverId
	 *            服务器Id
	 * @param iRedis TODO
	 * @param uuidTypePackageArr
	 * 			  UUID类型包数组
	 */
	UUIDGeneratorImpl(int serverGroup, int serverId, Class<? extends IUUIDType> enumClass, IRedis iRedis) {
		if(enumClass == null) {
			throw new IllegalArgumentException("uuidTypePakcageArr cannot be null!");
		}
		
		// 构建每一种类型的Id生成器
		for(IUUIDType uuidType : enumClass.getEnumConstants()) {
			final long oldUUID = uuidType.getOldMaxUuidFromRedis(iRedis, serverId);
			final UUID64Template tmpl = new UUID64Template(SERVERGROUP_BIT_NUM, SERVER_BIT_NUM, OBJECT_ID_BIT_NUM, serverGroup, serverId);
			
			long oldMaxUUID = oldUUID <= 0 ? tmpl.getMinUUID() : oldUUID;
			oldMaxUUID += UUID_STEP;
			
			uuids.put(uuidType, UUID64.buildUUID(oldMaxUUID, tmpl));
		}
	}

	@Override
	public long getNextUUID(IUUIDType uuidType) {
		if(!this.uuids.containsKey(uuidType)) {
			logger.warn("不存在" + uuidType + "对应的uuid类型！");
			return -1;
		}
		return this.uuids.get(uuidType).getNextUUID();
	}

}
