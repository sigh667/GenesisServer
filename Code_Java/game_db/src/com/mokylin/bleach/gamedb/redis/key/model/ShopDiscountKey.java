package com.mokylin.bleach.gamedb.redis.key.model;

import com.mokylin.bleach.gamedb.orm.entity.ShopDiscountEntity;
import com.mokylin.bleach.gamedb.redis.key.AbstractRedisKey;
import com.mokylin.bleach.gamedb.redis.key.IMultiRedisKey;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyType;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyWithServerId;
import com.mokylin.bleach.gamedb.redis.redisop.IEntityRedisOp;
import com.mokylin.bleach.gamedb.redis.redisop.model.EntityMultiKeyRedisOp;

public class ShopDiscountKey extends RedisKeyWithServerId<Long, ShopDiscountEntity> implements IMultiRedisKey<Long, ShopDiscountEntity> {

	private final int shopTypeId;
	
	public ShopDiscountKey(Integer serverId, Long id, int shopTypeId) {
		super(serverId, id);
		this.shopTypeId = shopTypeId;
	}

	@Override
	public String getKey() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getServerId());
		sb.append(AbstractRedisKey.separator);
		sb.append(RedisKeyType.ShopDiscount.toString());
		return sb.toString();
	}

	@Override
	public Class<ShopDiscountEntity> getEntityType() {
		return ShopDiscountEntity.class;
	}

	@Override
	public IEntityRedisOp getEntityRedisOp() {
		return EntityMultiKeyRedisOp.INSTANCE;
	}

	@Override
	public String getField() {
		return this.shopTypeId + "";
	}

}
