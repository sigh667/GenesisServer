package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class Name2Template extends Name2TemplateVO {

	@Override
	public void check() throws TemplateConfigException {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
