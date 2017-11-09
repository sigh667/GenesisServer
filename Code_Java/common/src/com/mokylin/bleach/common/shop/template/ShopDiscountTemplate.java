package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 物品打折模板
 * @author yaguang.xiao
 *
 */
@ExcelRowBinding
public class ShopDiscountTemplate extends ShopDiscountTemplateVO {

	@Override
	public void check() throws TemplateConfigException {
		
	}

}
