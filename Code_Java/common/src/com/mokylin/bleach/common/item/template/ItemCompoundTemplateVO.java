package com.mokylin.bleach.common.item.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 物品合成模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ItemCompoundTemplateVO extends TemplateObject {
	
	/** 合成价格 */
	@ExcelCellBinding(offset = 1)
	protected long compoundPrice;

	/** 合成需要的物品模板ID */
	@ExcelCollectionMapping(clazz = com.mokylin.bleach.common.item.template.ItemCompoundMaterial[].class, collectionNumber = "2,3;4,5;6,7;8,9")
	protected com.mokylin.bleach.common.item.template.ItemCompoundMaterial[] compoundMaterials;


	public long getCompoundPrice() {
		return this.compoundPrice;
	}

	public void setCompoundPrice(long compoundPrice) {
		if (compoundPrice == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[合成价格]compoundPrice不可以为0");
		}
		if (compoundPrice < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[合成价格]compoundPrice的值不得小于1");
		}
		this.compoundPrice = compoundPrice;
	}
	
	public com.mokylin.bleach.common.item.template.ItemCompoundMaterial[] getCompoundMaterials() {
		return this.compoundMaterials;
	}

	public void setCompoundMaterials(com.mokylin.bleach.common.item.template.ItemCompoundMaterial[] compoundMaterials) {
		if (compoundMaterials == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[合成需要的物品模板ID]compoundMaterials不可以为空");
		}	
		this.compoundMaterials = compoundMaterials;
	}
	

	@Override
	public String toString() {
		return "ItemCompoundTemplateVO[compoundPrice=" + compoundPrice + ",compoundMaterials=" + compoundMaterials + ",]";

	}
}