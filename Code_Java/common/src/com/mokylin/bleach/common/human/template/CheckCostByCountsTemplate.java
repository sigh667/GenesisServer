package com.mokylin.bleach.common.human.template;

import java.util.Map;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.human.CostByCountsType;
import com.mokylin.bleach.core.template.IAfterTemplateReady;
import com.mokylin.bleach.core.template.util.TemplateCheckUtil;

/**
 * 购买次数与购买价格模板
 * @author ChangXiao
 *
 */
public class CheckCostByCountsTemplate implements IAfterTemplateReady {
	@Override
	public void execute() {
		Map<Integer, CostByCountsTemplate> countsMap = GlobalData.getTemplateService().getAll(CostByCountsTemplate.class);
		TemplateCheckUtil.isSequenceTemplate(countsMap, 1,
				countsMap.size(), "消费次数配置不连续(必须从1级开始连续递增)");
		
		CostByCountsType[] types = CostByCountsType.values();
		
		//判断模板是否完结的boolean数组
		boolean[] typesIsFinished = new boolean[types.length];
		//获取每次购买的消耗
		for (int i = 1; i <= countsMap.size(); i++) {
			long[] costs = countsMap.get(i).getCostByCountsArray();
			//获取类型
			for (CostByCountsType type : types) {
				int index = type.ordinal();
				//如果检测到该配置类型完结后(配置项为0)，任然有配置项则配置异常
				if (typesIsFinished[index] && costs[index] > 0) {
					throw new RuntimeException(type + "配置错误，消耗数量必须连续配置，不能有间隔");
				}
				typesIsFinished[index] = (costs[index] <= 0);
			}
		}
	}

}
