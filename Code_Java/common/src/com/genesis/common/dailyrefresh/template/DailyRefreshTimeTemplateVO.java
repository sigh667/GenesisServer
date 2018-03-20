package com.genesis.common.dailyrefresh.template;

import ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;
import com.genesis.core.template.annotation.ExcelCollectionMapping;
import com.genesis.core.template.TemplateObject;

/**
 * 每日定时刷新模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class DailyRefreshTimeTemplateVO extends TemplateObject {
	
	/** 刷新的时间点，每日最多24个 */
	@ExcelCollectionMapping(clazz = String.class, collectionNumber = "2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25")
	protected String[] timeArray;


	public String[] getTimeArray() {
		return this.timeArray;
	}

	public void setTimeArray(String[] timeArray) {
		if (timeArray == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[刷新的时间点，每日最多24个]timeArray不可以为空");
		}	
		this.timeArray = timeArray;
	}
	

	@Override
	public String toString() {
		return "DailyRefreshTimeTemplateVO{timeArray=" + timeArray + ",}";

	}
}