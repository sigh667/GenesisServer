package com.genesis.gamedb.redis.key;

import com.genesis.gamedb.orm.EntityWithRedisKey;

import java.io.Serializable;

public interface IMultiRedisKey<IdType extends Serializable, T extends EntityWithRedisKey<?>>
        extends IRedisKey<IdType, T> {

    String getField();
}
