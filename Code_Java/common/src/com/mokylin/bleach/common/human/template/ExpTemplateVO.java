package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 经验等级模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ExpTemplateVO extends TemplateObject {
	
	/** 战队升级所需经验 */
	@ExcelCellBinding(offset = 1)
	protected long humanExp;

	/** 英雄升级所需经验 */
	@ExcelCellBinding(offset = 2)
	protected long heroExp;


	public long getHumanExp() {
		return this.humanExp;
	}

	public void setHumanExp(long humanExp) {
		if (humanExp == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[战队升级所需经验]humanExp不可以为0");
		}
		if (humanExp < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[战队升级所需经验]humanExp的值不得小于1");
		}
		this.humanExp = humanExp;
	}
	
	public long getHeroExp() {
		return this.heroExp;
	}

	public void setHeroExp(long heroExp) {
		if (heroExp == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[英雄升级所需经验]heroExp不可以为0");
		}
		if (heroExp < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[英雄升级所需经验]heroExp的值不得小于1");
		}
		this.heroExp = heroExp;
	}
	

	@Override
	public String toString() {
		return "ExpTemplateVO[humanExp=" + humanExp + ",heroExp=" + heroExp + ",]";

	}
}