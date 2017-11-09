package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 商店刷新模板
 * @author yaguang.xiao
 *
 */
@ExcelRowBinding
public class ShopRefreshPriceTemplate extends ShopRefreshPriceTemplateVO {

	@Override
	public void check() throws TemplateConfigException {
		// 检查数据完整性
		if(this.shopRefreshPriceArr.length < ShopType.values().length) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "数据不全");
		}
		
		// 检查每个商店的刷新价格
		for(ShopType shopType : ShopType.values()) {
			long refreshPrice = this.shopRefreshPriceArr[shopType.ordinal()];
			if(refreshPrice <= 0) {
				throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format(shopType + "的刷新价格【%d】必须大于0", refreshPrice));
			}
		}
	}
	
	/**
	 * 货物刷新的价格
	 * @param shopType
	 * @return
	 */
	public long getPrice(ShopType shopType) {
		if(shopType == null) {
			return 0;
		}
		
		return this.shopRefreshPriceArr[shopType.ordinal()];
	}
	
}
