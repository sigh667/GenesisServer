package com.mokylin.bleach.common.combat.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * 战斗类型模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class CombatTemplateVO extends TemplateObject {
	
	/** 名称 */
	@ExcelCellBinding(offset = 1)
	protected String name;

	/** 最大回合数 */
	@ExcelCellBinding(offset = 2)
	protected int maxRound;


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[名称]name不可以为空");
		}
		this.name = name;
	}
	
	public int getMaxRound() {
		return this.maxRound;
	}

	public void setMaxRound(int maxRound) {
		if (maxRound == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[最大回合数]maxRound不可以为0");
		}
		this.maxRound = maxRound;
	}
	

	@Override
	public String toString() {
		return "CombatTemplateVO[name=" + name + ",maxRound=" + maxRound + ",]";

	}
}