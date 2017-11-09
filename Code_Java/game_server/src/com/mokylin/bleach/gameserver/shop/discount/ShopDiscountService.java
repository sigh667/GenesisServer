package com.mokylin.bleach.gameserver.shop.discount;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.gamedb.orm.entity.ShopDiscountEntity;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;

public class ShopDiscountService {
	
	/** 无折扣 */
	public static final int NO_DISCOUNT = 10000;
	/** 没有翻倍 */
	public static final int NO_MULTIPLE = 1;
	
	/** 物品打折Map<商店类型, 物品打折信息> */
	private Map<ShopType, ShopDiscount> shopDiscounts = Maps.newConcurrentMap();
	
	/**
	 * 初始化
	 * @param shopDiscounts
	 * @param sGlobals
	 */
	public void init(Collection<ShopDiscountEntity> shopDiscounts, ServerGlobals sGlobals) {
		for(ShopDiscountEntity entity : shopDiscounts) {
			ShopDiscount shopDiscount = new ShopDiscount(sGlobals);
			shopDiscount.fromEntity(entity);
			if(shopDiscount.isEnded()) {
				shopDiscount.onDelete();
			} else {
				this.shopDiscounts.put(shopDiscount.getShopType(), shopDiscount);
			}
		}
	}
	
	/**
	 * 添加打折信息
	 * @param shopDiscount
	 */
	public void addDiscountInfo(ShopDiscount shopDiscount, ServerGlobals sGlobals) {
		if(shopDiscount == null) return;
		ShopDiscount oldDiscount = this.shopDiscounts.get(shopDiscount.getShopType());
		if(oldDiscount != null && oldDiscount.isEnded()) {
			oldDiscount.onDelete();
		} else if (oldDiscount != null && !oldDiscount.isEnded()) {
			return;
		}
		
		shopDiscount.setModified();
		this.shopDiscounts.put(shopDiscount.getShopType(), shopDiscount);
	}
	
	/**
	 * 获取商店打折信息
	 * @param shopType
	 * @return
	 */
	public ShopDiscount getShopDiscount(ShopType shopType) {
		
		return this.shopDiscounts.get(shopType);
	}

}
