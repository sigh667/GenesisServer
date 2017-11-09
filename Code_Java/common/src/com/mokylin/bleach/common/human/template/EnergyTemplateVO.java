package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 体力模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class EnergyTemplateVO extends TemplateObject {
	
	/** 玩家体力值 */
	@ExcelCellBinding(offset = 1)
	protected int energy;


	public int getEnergy() {
		return this.energy;
	}

	public void setEnergy(int energy) {
		if (energy == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[玩家体力值]energy不可以为0");
		}
		if (energy < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[玩家体力值]energy的值不得小于1");
		}
		this.energy = energy;
	}
	

	@Override
	public String toString() {
		return "EnergyTemplateVO[energy=" + energy + ",]";

	}
}