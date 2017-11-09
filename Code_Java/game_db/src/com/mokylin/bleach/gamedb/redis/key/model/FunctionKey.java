package com.mokylin.bleach.gamedb.redis.key.model;

import com.mokylin.bleach.gamedb.orm.entity.FunctionEntity;
import com.mokylin.bleach.gamedb.redis.key.AbstractRedisKey;
import com.mokylin.bleach.gamedb.redis.key.IMultiRedisKey;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyType;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyWithServerId;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;
import com.mokylin.bleach.gamedb.redis.redisop.model.EntityMultiKeyRedisOp;

public class FunctionKey extends RedisKeyWithServerId<Long, FunctionEntity> implements IMultiRedisKey<Long, FunctionEntity> {

	/** 玩家Id */
	private final long humanId;
	/** 功能Id */
	private final int functionId;
	
	public FunctionKey(Integer serverId, long humanId, int functionId, Long id) {
		super(serverId, id);
		this.humanId = humanId;
		this.functionId = functionId;
	}

	@Override
	public String getKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getServerId());
		sb.append(AbstractRedisKey.separator);
		sb.append(RedisKeyType.Function.toString());
		sb.append(AbstractRedisKey.separator);
		sb.append(humanId);
		return sb.toString();
	}

	@Override
	public Class<FunctionEntity> getEntityType() {
		return FunctionEntity.class;
	}

	@Override
	public IEntityRedisOp getEntityRedisOp() {
		return EntityMultiKeyRedisOp.INSTANCE;
	}

	@Override
	public String getField() {
		return String.valueOf(this.functionId);
	}

}
