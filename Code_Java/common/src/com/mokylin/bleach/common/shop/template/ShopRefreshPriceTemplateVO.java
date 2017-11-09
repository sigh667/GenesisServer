package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 商店刷新消耗模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ShopRefreshPriceTemplateVO extends TemplateObject {
	
	/** 每个商店的刷新价格 */
	@ExcelCollectionMapping(clazz = long.class, collectionNumber = "1;2")
	protected long[] shopRefreshPriceArr;


	public long[] getShopRefreshPriceArr() {
		return this.shopRefreshPriceArr;
	}

	public void setShopRefreshPriceArr(long[] shopRefreshPriceArr) {
		if (shopRefreshPriceArr == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[每个商店的刷新价格]shopRefreshPriceArr不可以为空");
		}	
		this.shopRefreshPriceArr = shopRefreshPriceArr;
	}
	

	@Override
	public String toString() {
		return "ShopRefreshPriceTemplateVO[shopRefreshPriceArr=" + shopRefreshPriceArr + ",]";

	}
}