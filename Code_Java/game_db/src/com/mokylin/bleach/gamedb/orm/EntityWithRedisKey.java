package com.mokylin.bleach.gamedb.orm;

import com.genesis.core.orm.BaseEntity;
import com.mokylin.bleach.gamedb.redis.key.IRedisKey;


public interface EntityWithRedisKey<T extends IRedisKey<?, ?>> extends BaseEntity {

    T newRedisKey(Integer serverId);
}
