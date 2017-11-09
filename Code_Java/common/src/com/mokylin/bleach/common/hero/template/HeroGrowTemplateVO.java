package com.mokylin.bleach.common.hero.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import java.util.List;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * hero成长模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class HeroGrowTemplateVO extends TemplateObject {
	
	/** 名称 */
	@ExcelCellBinding(offset = 1)
	protected String name;

	/** 每级成长的属性 */
	@ExcelCollectionMapping(clazz = com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col.class, collectionNumber = "2,3;4,5;6,7;8,9;10,11")
	protected List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col> attributeList;


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
	
	public List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col> getAttributeList() {
		return this.attributeList;
	}

	public void setAttributeList(List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col> attributeList) {
		if (attributeList == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[每级成长的属性]attributeList不可以为空");
		}	
		this.attributeList = attributeList;
	}
	

	@Override
	public String toString() {
		return "HeroGrowTemplateVO[name=" + name + ",attributeList=" + attributeList + ",]";

	}
}