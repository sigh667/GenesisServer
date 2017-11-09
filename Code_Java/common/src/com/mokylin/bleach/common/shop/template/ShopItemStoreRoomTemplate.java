package com.mokylin.bleach.common.shop.template;

import java.util.Collections;
import java.util.List;

import com.mokylin.bleach.common.item.ItemType;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 物品库模板
 * @author yaguang.xiao
 *
 */
@ExcelRowBinding
public class ShopItemStoreRoomTemplate extends ShopItemStoreRoomTemplateVO {

	/** 本库的物品类型列表 */
	private List<ItemType> itemTypeList;
	
	@Override
	public void patchUp() throws Exception {
		super.patchUp();
		this.itemTypeList = Collections.unmodifiableList(ItemType.getByItemTypeId(itemType));
	}

	@Override
	public void check() throws TemplateConfigException {
		if(!ItemType.isValid(this.itemType)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format("itemType[%d]无效", this.itemType));
		}
		
		if(this.itemLevelScope < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format("itemLevelScope[%d]不能小于0", this.itemLevelScope));
		}
		
		if(this.numberMultiple <= 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(), String.format("numberMultiple[%d]必须大于0", this.numberMultiple));
		}
	}
	
	/**
	 * 获取本库的物品类型列表（此列表不能被修改，否则会抛出异常）
	 * @return
	 */
	public List<ItemType> getItemTypeList() {
		return this.itemTypeList;
	}

}
