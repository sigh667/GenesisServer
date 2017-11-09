package com.mokylin.bleach.gamedb.redis.key.model;

import com.mokylin.bleach.gamedb.orm.entity.HeroEntity;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyWithServerId;
import com.mokylin.bleach.gamedb.redis.key.IMultiRedisKey;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyType;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;
import com.mokylin.bleach.gamedb.redis.redisop.model.EntityMultiKeyRedisOp;

public class HeroKey extends RedisKeyWithServerId<Long, HeroEntity> implements IMultiRedisKey<Long, HeroEntity>{
	
	/**所属角色ID*/
	private final long humanId;

	public HeroKey(Integer serverId, long humanId, long heroId) {
		super(serverId, heroId);
		this.humanId = humanId;
	}

	@Override
	public String getKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(getServerId());
		sb.append(RedisKeyWithServerId.separator);
		sb.append(RedisKeyType.Hero.toString());
		sb.append(RedisKeyWithServerId.separator);
		sb.append(humanId);
		return sb.toString();
	}

	@Override
	public Class<HeroEntity> getEntityType() {
		return HeroEntity.class;
	}

	@Override
	public IEntityRedisOp getEntityRedisOp() {
		return EntityMultiKeyRedisOp.INSTANCE;
	}

	@Override
	public String getField() {
		return this.getDbId().toString();
	}

}
