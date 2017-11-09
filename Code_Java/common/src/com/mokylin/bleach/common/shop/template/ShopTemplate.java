package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 商店模板
 * @author yaguang.xiao
 *
 */
@ExcelRowBinding
public class ShopTemplate extends ShopTemplateVO {

	/** 商店类型 */
	private ShopType shopType;
	
	@Override
	public void patchUp() throws Exception {
		super.patchUp();
		
		this.shopType = ShopType.getByIndex(this.id);
	}

	@Override
	public void check() throws TemplateConfigException {
		
		if(this.shopType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format("商店类型【%d】无效", this.id));
		}
		
		if(this.tempOpenDuration < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format("临时开启时长【%d】不能为负数", this.tempOpenDuration));
		}
	}

	/**
	 * 商店类型
	 * @return
	 */
	public ShopType getShopType() {
		return shopType;
	}
}
