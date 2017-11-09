package com.mokylin.bleach.core.template;

import com.mokylin.bleach.core.config.exception.ConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 所有template的父类 <p>
 * 
 * 这个里面只包含template的唯一ID
 * 
 */
@ExcelRowBinding
public abstract class TemplateObject {

	public static int NULL_ID = 0;

	@ExcelCellBinding(offset = 0)
	protected int id;

	protected String sheetName;

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}
	
	public final void throwTemplateException(String errMsg){
		throw new TemplateConfigException(this.sheetName, this.id, errMsg);
	}

	/**
	 * <pre>
	 * 在{@link TemplateService}加载完所有的模板对象之后调用，主要用于检查各个模板
	 * 表配置是否正确，如果不正确，应抛出{@link ConfigException}类型的异常，并在异常
	 * 消息中记录详细的出错信息。
	 * 先调用所有模板的patchUp()，再调用所有模板的check()
	 * </pre>
	 * 
	 * 
	 * @throws TemplateConfigException
	 */
	public abstract void check() throws TemplateConfigException;

	/**
	 * <pre>
	 * 在{@link TemplateService}加载完所有的模板对象之后调用，主要用于构建各个模板
	 * 对象之间的依赖关系。
	 * 先调用所有模板的patchUp()，再调用所有模板的check()
	 * </pre>
	 * 
	 * @throws Exception
	 */
	public void patchUp() throws Exception {
	}

	/**
	 * 返回此模板的名字，可以写的更详细一点，哪个文件的那个页签
	 * 
	 * @return
	 */
	public String getSheetName() {
		return this.sheetName;
	}

	/**
	 * 设置此模版所对应的 Excel 页签名称
	 * 
	 * @param value
	 */
	public void setSheetName(String value) {
		this.sheetName = value;
	}
}
