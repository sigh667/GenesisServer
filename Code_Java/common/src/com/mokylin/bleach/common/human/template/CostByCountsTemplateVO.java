package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 随次数改变的钻石消耗数量模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class CostByCountsTemplateVO extends TemplateObject {
	
	/** 购买次数与购买价格模板 */
	@ExcelCollectionMapping(clazz = long.class, readAll=true, collectionNumber = "1;2;3;4;5;6;7")
	protected long[] costByCountsArray;


	public long[] getCostByCountsArray() {
		return this.costByCountsArray;
	}

	public void setCostByCountsArray(long[] costByCountsArray) {
		if (costByCountsArray == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[购买次数与购买价格模板]costByCountsArray不可以为空");
		}	
		this.costByCountsArray = costByCountsArray;
	}
	

	@Override
	public String toString() {
		return "CostByCountsTemplateVO[costByCountsArray=" + costByCountsArray + ",]";

	}
}