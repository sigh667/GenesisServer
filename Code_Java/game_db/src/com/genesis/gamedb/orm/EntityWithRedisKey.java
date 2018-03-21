package com.genesis.gamedb.orm;

import com.genesis.core.orm.BaseEntity;
import com.genesis.gamedb.redis.key.IRedisKey;


public interface EntityWithRedisKey<T extends IRedisKey<?, ?>> extends BaseEntity {

    T newRedisKey(Integer serverId);
}
