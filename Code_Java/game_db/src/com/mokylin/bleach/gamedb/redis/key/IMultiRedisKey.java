package com.mokylin.bleach.gamedb.redis.key;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;

import java.io.Serializable;

public interface IMultiRedisKey<IdType extends Serializable, T extends EntityWithRedisKey<?>>
        extends IRedisKey<IdType, T> {

    String getField();
}
