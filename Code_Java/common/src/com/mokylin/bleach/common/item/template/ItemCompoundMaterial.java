package com.mokylin.bleach.common.item.template;

import com.mokylin.bleach.core.template.annotation.BeanFieldNumber;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;

@ExcelRowBinding
public class ItemCompoundMaterial {

	/** 原材料物品的模板ID */
	@BeanFieldNumber(number = 0)
	private int materialTemplateId;
	
	/** 所需数量 */
	@BeanFieldNumber(number = 1)
	private int amount;

	public int getMaterialTemplateId() {
		return materialTemplateId;
	}

	public void setMaterialTemplateId(int materialTemplateId) {
		this.materialTemplateId = materialTemplateId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
