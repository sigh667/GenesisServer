package com.genesis.gamedb.redis.redisop.model;

import com.genesis.core.redis.op.PipelineProcess;
import com.genesis.gamedb.orm.EntityWithRedisKey;
import com.genesis.gamedb.redis.key.IMultiRedisKey;
import com.genesis.gamedb.redis.key.IRedisKey;
import com.genesis.gamedb.redis.redisop.IEntityRedisOp;

import org.hibernate.metamodel.source.annotations.entity.IdType;

import java.io.Serializable;

/**
 * 针对第二层结构为hash的实现
 * @author Joey
 *
 */
@SuppressWarnings("unchecked")
public enum EntityMultiKeyRedisOp implements IEntityRedisOp {
    INSTANCE;

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void getEntityFromRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
        IMultiRedisKey<IdType, T> mulkey = (IMultiRedisKey<IdType, T>) key;
        pipelineProcess.getHashOp()
                .hget(mulkey.getKey(), mulkey.getField(), mulkey.getEntityType());
    }

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void flushToRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key, T entity) {
        IMultiRedisKey<IdType, T> mulkey = (IMultiRedisKey<IdType, T>) key;
        pipelineProcess.getHashOp().hset(mulkey.getKey(), mulkey.getField(), entity);
    }

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void deleteFromRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
        IMultiRedisKey<IdType, T> mulkey = (IMultiRedisKey<IdType, T>) key;
        pipelineProcess.getHashOp().hdel(mulkey.getKey(), mulkey.getField());
    }

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void getAllEntities(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
        pipelineProcess.getHashOp().hgetall(key.getKey(), key.getEntityType());
    }

}
