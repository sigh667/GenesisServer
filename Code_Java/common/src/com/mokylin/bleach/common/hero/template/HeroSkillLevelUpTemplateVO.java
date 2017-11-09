package com.mokylin.bleach.common.hero.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * Hero技能升级费用模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class HeroSkillLevelUpTemplateVO extends TemplateObject {
	
	/** 大招技能升级费用 */
	@ExcelCellBinding(offset = 1)
	protected long specialSkillCost;

	/** 辅助技费用 */
	@ExcelCollectionMapping(clazz = long.class, collectionNumber = "2;3;4;5;6")
	protected long[] skillCost;


	public long getSpecialSkillCost() {
		return this.specialSkillCost;
	}

	public void setSpecialSkillCost(long specialSkillCost) {
		if (specialSkillCost == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[大招技能升级费用]specialSkillCost不可以为0");
		}
		this.specialSkillCost = specialSkillCost;
	}
	
	public long[] getSkillCost() {
		return this.skillCost;
	}

	public void setSkillCost(long[] skillCost) {
		if (skillCost == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[辅助技费用]skillCost不可以为空");
		}	
		this.skillCost = skillCost;
	}
	

	@Override
	public String toString() {
		return "HeroSkillLevelUpTemplateVO[specialSkillCost=" + specialSkillCost + ",skillCost=" + skillCost + ",]";

	}
}