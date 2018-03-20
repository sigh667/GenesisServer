package com.genesis.common.hero.template;

import com.genesis.core.template.annotation.ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;
import com.genesis.core.template.TemplateObject;
import com.genesis.core.template.annotation.ExcelCellBinding;

/**
 * Hero技能等级上限模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class HeroSkillLevelMaxTemplateVO extends TemplateObject {
	
	/** 最高能升到Human等级减多少 */
	@ExcelCellBinding(offset = 2)
	protected long belowHumanLevel;


	public long getBelowHumanLevel() {
		return this.belowHumanLevel;
	}

	public void setBelowHumanLevel(long belowHumanLevel) {
		if (belowHumanLevel < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[最高能升到Human等级减多少]belowHumanLevel的值不得小于0");
		}
		this.belowHumanLevel = belowHumanLevel;
	}
	

	@Override
	public String toString() {
		return "HeroSkillLevelMaxTemplateVO{belowHumanLevel=" + belowHumanLevel + ",}";

	}
}