package com.mokylin.bleach.gameserver.shop.discount.init;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.mokylin.bleach.gamedb.orm.entity.ShopDiscountEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ShopDiscountKey;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.serverinit.ServerInitFunction;

/**
 * 商店打折初始化服务
 * @author yaguang.xiao
 *
 */
public class ShopDiscountInitFunc extends ServerInitFunction<ShopDiscountInitResult> {

	@Override
	public ShopDiscountInitResult apply(ServerGlobals sGlobals) {
		// 从Redis里面加载物品打折信息
		ShopDiscountKey shopDiscountKey = new ShopDiscountKey(sGlobals.getServerId(), 0l, 0);
		Map<String, ShopDiscountEntity> map = sGlobals.getRedis().getHashOp().hgetall(shopDiscountKey.getKey(), ShopDiscountEntity.class).get();
		Collection<ShopDiscountEntity> shopDiscountEntities;
		if(map == null || map.isEmpty()) {
			shopDiscountEntities = Collections.emptyList();
		} else {
			shopDiscountEntities = map.values();
		}
		
		return new ShopDiscountInitResult(shopDiscountEntities);
	}

	@Override
	public void set(ShopDiscountInitResult result, ServerGlobals sGlobals) {
		sGlobals.getDiscountService().init(result.shopDiscounts, sGlobals);
	}

}
