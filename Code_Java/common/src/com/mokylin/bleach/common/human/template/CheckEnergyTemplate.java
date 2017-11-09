package com.mokylin.bleach.common.human.template;

import java.util.Map;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.template.IAfterTemplateReady;
import com.mokylin.bleach.core.template.util.TemplateCheckUtil;

/**
 * 检查体力模板
 * @author ChangXiao
 *
 */
public class CheckEnergyTemplate implements IAfterTemplateReady {

	@Override
	public void execute() {
		Map<Integer, EnergyTemplate> energyMap = GlobalData.getTemplateService().getAll(EnergyTemplate.class);
		TemplateCheckUtil.isSequenceTemplate(energyMap, 1,
				energyMap.size(), "体力等级配置不连续(Level必须从1级开始连续递增)");
	}

}
