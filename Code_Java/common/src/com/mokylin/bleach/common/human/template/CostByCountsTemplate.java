package com.mokylin.bleach.common.human.template;

import com.mokylin.bleach.common.human.CostByCountsType;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class CostByCountsTemplate extends CostByCountsTemplateVO {
	private static int[] TYPE_LENGTH_ARRAY = new int[CostByCountsType.values().length];
	
	public static int getLength(CostByCountsType type){
		return TYPE_LENGTH_ARRAY[type.ordinal()];
	}
	
	@Override
	public void patchUp() throws Exception {
		int id = this.getId();
		if (this.getCostByCountsArray().length < TYPE_LENGTH_ARRAY.length) return;
		
		long[] costs = this.getCostByCountsArray();
		for (int i = 0; i < TYPE_LENGTH_ARRAY.length; i++) {
			if (id > TYPE_LENGTH_ARRAY[i] && costs[i] > 0) {
				TYPE_LENGTH_ARRAY[i] = id;
			}
		}
	}

	@Override
	public void check() throws TemplateConfigException {
		//模板中配置的数量 必须>= CostDiamondByTimesType枚举数量
		if (this.getCostByCountsArray().length < TYPE_LENGTH_ARRAY.length) {
			throwTemplateException("CostByTimes模板配置错误，配置的类型数量小于程序定义的数量");
		}
	}

}
