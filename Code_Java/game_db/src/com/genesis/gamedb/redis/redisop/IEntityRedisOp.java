package com.genesis.gamedb.redis.redisop;

import com.genesis.core.redis.op.PipelineProcess;
import com.genesis.gamedb.orm.EntityWithRedisKey;
import com.genesis.gamedb.redis.key.IRedisKey;

import java.io.Serializable;

public interface IEntityRedisOp {

    /**
     * 从Redis中取出本key所对应的所有Entity
     * @param pipelineProcess
     * @param key
     * @return
     */
    <V extends Serializable, T extends EntityWithRedisKey<?>> void getEntityFromRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key);

    /**
     * 将实体对象存入Redis
     * @param pipelineProcess
     * @param key
     * @param entity
     */
    <V extends Serializable, T extends EntityWithRedisKey<?>> void flushToRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key, T entity);

    /**
     * 将本key对应的数据从Redis中删除
     * @param pipelineProcess
     * @param key
     */
    <V extends Serializable, T extends EntityWithRedisKey<?>> void deleteFromRedis(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key);

    /**
     * 取key所对应的所有数据，有可能是一个value、也有可能是整个hash
     * @param pipelineProcess
     * @param key
     * @return
     */
    <V extends Serializable, T extends EntityWithRedisKey<?>> void getAllEntities(
            PipelineProcess pipelineProcess, IRedisKey<V, T> key);
}
