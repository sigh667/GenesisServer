package com.genesis.common.shop.template;

import ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;
import com.genesis.core.template.annotation.ExcelCollectionMapping;
import com.genesis.core.template.TemplateObject;

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
		return "ShopRefreshPriceTemplateVO{shopRefreshPriceArr=" + shopRefreshPriceArr + ",}";

	}
}