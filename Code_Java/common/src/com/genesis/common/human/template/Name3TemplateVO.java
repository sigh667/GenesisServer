package com.genesis.common.human.template;

import com.genesis.core.template.annotation.ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;
import com.genesis.core.template.TemplateObject;
import com.genesis.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户名称后缀
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class Name3TemplateVO extends TemplateObject {
	
	/** User PostName */
	@ExcelCellBinding(offset = 1)
	protected String name;


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[User PostName]name不可以为空");
		}
		this.name = name;
	}
	

	@Override
	public String toString() {
		return "Name3TemplateVO{name=" + name + ",}";

	}
}