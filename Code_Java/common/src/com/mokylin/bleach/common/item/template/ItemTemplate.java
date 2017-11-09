package com.mokylin.bleach.common.item.template;

import com.mokylin.bleach.common.item.ItemTemplateCheckUtil;
import com.mokylin.bleach.common.item.ItemType;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class ItemTemplate extends ItemTemplateVO {
	
	@Override
	public void check() throws TemplateConfigException {
		//检查物品卖出价格是否合法，必须为正整数
		if(this.getSellPrice() <= 0){
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品卖出价格必须为正整数！");
		}
		//检查类型为碎片的物品必须在合成表中存在
		if(getItemType() == ItemType.EquipmentPieces || getItemType() == ItemType.EquipmentMaterialPieces){
			checkIsMaterial();
		}
		
		//TODO:验证掉落副本ID是否存在
	}
	
	private void checkIsMaterial() {
		if(!ItemTemplateCheckUtil.isMaterial(this.id)){
			throw new TemplateConfigException(this.getSheetName(), this.getId(), "物品类型为碎片，但是在合成表中没有该物品作为合成材料的信息！");
		}
	}
}
