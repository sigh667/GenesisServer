package com.mokylin.bleach.common.combat.model;

import com.mokylin.bleach.common.combat.enumeration.EffectID;
import com.mokylin.bleach.core.template.annotation.BeanFieldNumber;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;

@ExcelRowBinding
public class EffectBase {

	/** 效果器ID */
	@BeanFieldNumber(number = 0)
	private EffectID effectName;
	
	@ExcelCollectionMapping(clazz = int.class, collectionNumber = "1;2;3;4;5")
	private int[] params;

	public EffectID getEffectName() {
		return effectName;
	}

	public void setEffectName(EffectID effectName) {
		this.effectName = effectName;
	}

	public int[] getParams() {
		return params;
	}

	public void setParams(int[] params) {
		this.params = params;
	}
}
