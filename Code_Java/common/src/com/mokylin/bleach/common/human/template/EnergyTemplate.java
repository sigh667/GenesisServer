package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class EnergyTemplate extends EnergyTemplateVO {

	@Override
	public void check() throws TemplateConfigException {
		int maxEnergy = GlobalData.getConstants().getMaxEnergy();
		//等级对应体力值不能大于全局体力上限
		if (this.getEnergy() > maxEnergy) {
			throwTemplateException(String.format("等级对应体力上限 【%d】必须 <= 全局体力上限【%d】",
							this.getEnergy(), maxEnergy));
		}

	}

}
