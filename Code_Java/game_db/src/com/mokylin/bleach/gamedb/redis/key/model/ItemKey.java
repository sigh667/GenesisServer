package com.mokylin.bleach.gamedb.redis.key.model;

import com.mokylin.bleach.gamedb.orm.entity.ItemEntity;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyWithServerId;
import com.mokylin.bleach.gamedb.redis.key.IMultiRedisKey;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyType;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;
import com.mokylin.bleach.gamedb.redis.redisop.model.EntityMultiKeyRedisOp;

public class ItemKey extends RedisKeyWithServerId<Long,ItemEntity> implements IMultiRedisKey<Long,ItemEntity> {
	
	/**所属角色ID*/
	private final long humanId;

	public ItemKey(Integer serverId, long humanId, long itemId) {
		super(serverId, itemId);
		this.humanId = humanId;
	}

	@Override
	public String getKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(getServerId());
		sb.append(RedisKeyWithServerId.separator);
		sb.append(RedisKeyType.Item.toString());
		sb.append(RedisKeyWithServerId.separator);
		sb.append(humanId);
		return sb.toString();
	}

	@Override
	public Class<ItemEntity> getEntityType() {
		return ItemEntity.class;
	}

	@Override
	public String getField() {
		return this.getDbId().toString();
	}

	@Override
	public IEntityRedisOp getEntityRedisOp() {
		return EntityMultiKeyRedisOp.INSTANCE;
	}

}
