package com.mokylin.bleach.gamedb.redis.key.model;

import com.mokylin.bleach.gamedb.orm.entity.ShopEntity;
import com.mokylin.bleach.gamedb.redis.key.AbstractRedisKey;
import com.mokylin.bleach.gamedb.redis.key.IMultiRedisKey;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyType;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyWithServerId;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;
import com.mokylin.bleach.gamedb.redis.redisop.model.EntityMultiKeyRedisOp;

public class ShopKey extends RedisKeyWithServerId<Long, ShopEntity> implements IMultiRedisKey<Long,ShopEntity> {

	/**所属角色ID*/
	private final long humanId;
	/** 商店类型 */
	private final int shopType;
	
	/**
	 * 
	 * @param serverId	服务器Id
	 * @param humanId	角色Id
	 * @param id		商店Id
	 */
	public ShopKey(int serverId, long humanId, int shopType, long id) {
		super(serverId, id);
		this.humanId = humanId;
		this.shopType = shopType;
	}

	@Override
	public String getKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getServerId());
		sb.append(AbstractRedisKey.separator);
		sb.append(RedisKeyType.Shop.toString());
		sb.append(AbstractRedisKey.separator);
		sb.append(humanId);
		return sb.toString();
	}

	@Override
	public Class<ShopEntity> getEntityType() {
		return ShopEntity.class;
	}

	@Override
	public IEntityRedisOp getEntityRedisOp() {
		return EntityMultiKeyRedisOp.INSTANCE;
	}

	@Override
	public String getField() {
		return String.valueOf(this.shopType);
	}

}
