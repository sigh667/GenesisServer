package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class ExpTemplate extends ExpTemplateVO {

	public static enum ExpEnum {
		HUMAN, HERO
	}

	public ExpTemplate() {
	}

	public ExpTemplate(long humanExp, long heroExp) {
		this.humanExp = humanExp;
		this.heroExp = heroExp;
	}

	@Override
	public void check() throws TemplateConfigException {
		// TODO Auto-generated method stub

	}

	public long getExp(ExpEnum e) {
		return ExpEnum.HUMAN == e ? getHumanExp() : getHeroExp();
	}
}
