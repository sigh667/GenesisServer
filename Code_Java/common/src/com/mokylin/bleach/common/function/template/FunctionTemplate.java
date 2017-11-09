package com.mokylin.bleach.common.function.template;

import com.mokylin.bleach.common.function.FunctionType;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 功能模板
 * @author yaguang.xiao
 *
 */
@ExcelRowBinding
public class FunctionTemplate extends FunctionTemplateVO {
	
	private FunctionType funcType;
	
	@Override
	public void patchUp() throws Exception {
		super.patchUp();
		
		this.funcType = FunctionType.valueOf(this.id);
	}

	@Override
	public void check() throws TemplateConfigException {
		if(this.funcType == null)
			this.throwTemplateException(String.format("功能类型【%d】无效", this.id));
	}
	
	/**
	 * 获取功能类型
	 * @return
	 */
	public FunctionType getFuncType() {
		return this.funcType;
	}
}
