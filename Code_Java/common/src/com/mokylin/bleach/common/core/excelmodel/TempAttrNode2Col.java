package com.mokylin.bleach.common.core.excelmodel;

import com.mokylin.bleach.core.template.annotation.BeanFieldNumber;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;

@ExcelRowBinding
public class TempAttrNode2Col {
	/** 属性ID */
	@BeanFieldNumber(number = 0)
	private String attributeIndex;
	/** 属性值 */
	@BeanFieldNumber(number = 1)
	private int attributeValue;

	public String getAttributeIndex() {
		return attributeIndex;
	}
	public void setAttributeIndex(String attributeIndex) {
		this.attributeIndex = attributeIndex;
	}
	public int getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(int attributeValue) {
		this.attributeValue = attributeValue;
	}
}
