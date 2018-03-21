package com.genesis.gamedb.redis.redisop.model;

import com.genesis.core.redis.op.PipelineProcess;
import com.genesis.gamedb.orm.EntityWithRedisKey;
import com.genesis.gamedb.redis.key.IRedisKey;
import com.genesis.gamedb.redis.redisop.IEntityRedisOp;

import java.io.Serializable;

/**
 * 针对第二层结构为Set的实现
 * @author Joey
 *
 */
public enum EntitySetRedisOp implements IEntityRedisOp {
    INSTANCE;

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void getEntityFromRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
        //这里是随机的pop一个元素，目前没有想到此种组合的应用场景
        pipelineProcess.getSetOp().spop(key.getKey(), key.getEntityType());
    }

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void flushToRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key, T entity) {
        pipelineProcess.getSetOp().sadd(key.getKey(), entity);
    }

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void deleteFromRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
        //这个和get相比，只是没要返回值而已...
        pipelineProcess.getSetOp().spop(key.getKey(), key.getEntityType());
    }

    @Override
    public <V extends Serializable, T extends EntityWithRedisKey<?>> void getAllEntities(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
        pipelineProcess.getSetOp().smembers(key.getKey(), key.getEntityType()).get();
    }

}
