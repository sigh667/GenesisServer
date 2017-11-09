package com.mokylin.bleach.gamedb.redis.key.model;

import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyWithServerId;
import com.mokylin.bleach.gamedb.redis.key.IMultiRedisKey;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyType;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;
import com.mokylin.bleach.gamedb.redis.redisop.model.EntityMultiKeyRedisOp;

public class ArenaSnapKey extends RedisKeyWithServerId<Long, ArenaSnapEntity> implements IMultiRedisKey<Long, ArenaSnapEntity> {

	public ArenaSnapKey(Integer serverId, Long id) {
		super(serverId, id);
	}

	@Override
	public String getKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(getServerId());
		sb.append(RedisKeyWithServerId.separator);
		sb.append(RedisKeyType.ArenaSnap.toString());
		return sb.toString();
	}

	@Override
	public Class<ArenaSnapEntity> getEntityType() {
		return ArenaSnapEntity.class;
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
