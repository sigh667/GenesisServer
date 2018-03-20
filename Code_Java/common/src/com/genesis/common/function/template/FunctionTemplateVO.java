package com.genesis.common.function.template;

import ExcelRowBinding;
import com.genesis.core.template.TemplateObject;
import com.genesis.core.template.annotation.ExcelCellBinding;

/**
 * 功能模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class FunctionTemplateVO extends TemplateObject {
	
	/** 是否默认开放 */
	@ExcelCellBinding(offset = 1)
	protected boolean defaultOpen;


	public boolean isDefaultOpen() {
		return this.defaultOpen;
	}

	public void setDefaultOpen(boolean defaultOpen) {
		this.defaultOpen = defaultOpen;
	}
	

	@Override
	public String toString() {
		return "FunctionTemplateVO{defaultOpen=" + defaultOpen + ",}";

	}
}