package com.genesis.gamedb.redis.key;

import com.genesis.gamedb.redis.redisop.IEntityRedisOp;
import com.genesis.gamedb.orm.EntityWithRedisKey;

import java.io.Serializable;

public interface IRedisKey<IdType extends Serializable, T extends EntityWithRedisKey<?>> {

    public static final String separator = ":";


    String getKey();

    Class<T> getEntityType();

    IdType getDbId();

    IEntityRedisOp getEntityRedisOp();
}
