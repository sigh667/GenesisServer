package com.mokylin.bleach.gamedb.redis.redisop.model;

import java.io.Serializable;

import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.redis.key.IRedisKey;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;

public enum EntityValueRedisOp implements IEntityRedisOp {

	INSTANCE;
	
	@Override
	public <V extends Serializable, T extends EntityWithRedisKey<?>> void getEntityFromRedis(PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
		pipelineProcess.getValueOp().get(key.getKey(), key.getEntityType());
	}

	@Override
	public <V extends Serializable, T extends EntityWithRedisKey<?>> void flushToRedis(PipelineProcess pipelineProcess, IRedisKey<V, T> key, T entity) {
		pipelineProcess.getValueOp().set(key.getKey(), entity);
	}

	@Override
	public <V extends Serializable, T extends EntityWithRedisKey<?>> void deleteFromRedis(PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
		pipelineProcess.getValueOp().del(key.getKey());
	}

	@Override
	public <V extends Serializable, T extends EntityWithRedisKey<?>> void getAllEntities(PipelineProcess pipelineProcess, IRedisKey<V, T> key) {
		pipelineProcess.getValueOp().get(key.getKey(), key.getEntityType());
	}

}
