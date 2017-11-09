package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 物品打折模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ShopDiscountTemplateVO extends TemplateObject {
	
	/** 图标Id */
	@ExcelCellBinding(offset = 1)
	protected int iconId;


	public int getIconId() {
		return this.iconId;
	}

	public void setIconId(int iconId) {
		if (iconId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[图标Id]iconId不可以为0");
		}
		this.iconId = iconId;
	}
	

	@Override
	public String toString() {
		return "ShopDiscountTemplateVO[iconId=" + iconId + ",]";

	}
}