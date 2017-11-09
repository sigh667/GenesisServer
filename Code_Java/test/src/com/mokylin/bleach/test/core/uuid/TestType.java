package com.mokylin.bleach.test.core.uuid;

import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.uuid.type.IUUIDType;

/**
 * 测试类型
 * 
 * @author yaguang.xiao
 *
 */

public enum TestType implements IUUIDType {
	Test,
	;

	@Override
	public long getOldMaxUuidFromRedis(IRedis iRedis, int serverId) {
		return 0;
	}

	@Override
	public long qurryOldMaxUuidFromDB(HibernateDBService dbService, int serverGroup, int serverId) {
		return 0;
	}

	@Override
	public void putOldMaxUuidIntoRedis(IRedis iRedis, int serverId, Long oldMaxId) {
	}

}
