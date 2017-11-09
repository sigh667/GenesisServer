package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 物品价格模板
 * @author yaguang.xiao
 *
 */
@ExcelRowBinding
public class ShopItemPriceTemplate extends ShopItemPriceTemplateVO {

	@Override
	public void patchUp() throws Exception {
		super.patchUp();
	}

	@Override
	public void check() throws TemplateConfigException {
		if(generalShopCurrencyType != Currency.GOLD && generalShopCurrencyType != Currency.DIAMOND) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format("普通商店货币类型id：%d非法", generalShopCurrencyType));
		}
		
		if(this.generalShopPrice < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format("普通商店货物价格【%d】不能为负数", this.generalShopPrice));
		}
	}
}
