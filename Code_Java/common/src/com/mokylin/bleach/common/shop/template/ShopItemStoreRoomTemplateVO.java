package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 物品库模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ShopItemStoreRoomTemplateVO extends TemplateObject {
	
	/** 物品类型掩码 */
	@ExcelCellBinding(offset = 1)
	protected int itemType;

	/** 物品等级区间 */
	@ExcelCellBinding(offset = 2)
	protected int itemLevelScope;

	/** 组数量倍数 */
	@ExcelCellBinding(offset = 3)
	protected int numberMultiple;


	public int getItemType() {
		return this.itemType;
	}

	public void setItemType(int itemType) {
		if (itemType == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[物品类型掩码]itemType不可以为0");
		}
		this.itemType = itemType;
	}
	
	public int getItemLevelScope() {
		return this.itemLevelScope;
	}

	public void setItemLevelScope(int itemLevelScope) {
		this.itemLevelScope = itemLevelScope;
	}
	
	public int getNumberMultiple() {
		return this.numberMultiple;
	}

	public void setNumberMultiple(int numberMultiple) {
		this.numberMultiple = numberMultiple;
	}
	

	@Override
	public String toString() {
		return "ShopItemStoreRoomTemplateVO[itemType=" + itemType + ",itemLevelScope=" + itemLevelScope + ",numberMultiple=" + numberMultiple + ",]";

	}
}