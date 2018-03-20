package com.genesis.gameserver.shop.discount.init;

import com.mokylin.bleach.gamedb.orm.entity.ShopDiscountEntity;

import java.util.Collection;

/**
 * 商店打折初始化结果
 * @author Administrator
 *
 */
public class ShopDiscountInitResult {

    /** 商店打折实体对象 */
    public final Collection<ShopDiscountEntity> shopDiscounts;

    public ShopDiscountInitResult(Collection<ShopDiscountEntity> shopDiscounts) {
        this.shopDiscounts = shopDiscounts;
    }

}
