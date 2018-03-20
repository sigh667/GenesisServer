package com.genesis.common.human.template;

import ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;
import com.genesis.core.template.TemplateObject;
import com.genesis.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户名称中缀
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class Name2TemplateVO extends TemplateObject {
	
	/** User InName */
	@ExcelCellBinding(offset = 1)
	protected String name;


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[User InName]name不可以为空");
		}
		this.name = name;
	}
	

	@Override
	public String toString() {
		return "Name2TemplateVO{name=" + name + ",}";

	}
}