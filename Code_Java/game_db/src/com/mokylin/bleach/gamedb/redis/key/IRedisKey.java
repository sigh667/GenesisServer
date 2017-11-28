package com.mokylin.bleach.gamedb.redis.key;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;

import java.io.Serializable;

public interface IRedisKey<IdType extends Serializable, T extends EntityWithRedisKey<?>> {

    public static final String separator = ":";


    String getKey();

    Class<T> getEntityType();

    IdType getDbId();

    IEntityRedisOp getEntityRedisOp();
}
