package com.genesis.test.core.uuid;

import com.genesis.core.orm.hibernate.HibernateDBService;
import com.genesis.core.redis.IRedis;
import com.genesis.core.uuid.type.IUUIDType;

/**
 * 测试类型
 *
 * @author yaguang.xiao
 *
 */

public enum TestType implements IUUIDType {
    Test,;

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
