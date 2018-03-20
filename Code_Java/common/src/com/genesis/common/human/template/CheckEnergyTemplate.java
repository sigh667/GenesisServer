package com.genesis.common.human.template;

import com.genesis.common.core.GlobalData;
import com.mokylin.bleach.core.template.IAfterTemplateReady;
import com.mokylin.bleach.core.template.util.TemplateCheckUtil;

import java.util.Map;

/**
 * 检查体力模板
 * @author ChangXiao
 *
 */
public class CheckEnergyTemplate implements IAfterTemplateReady {

    @Override
    public void execute() {
        Map<Integer, EnergyTemplate> energyMap =
                GlobalData.getTemplateService().getAll(EnergyTemplate.class);
        TemplateCheckUtil
                .isSequenceTemplate(energyMap, 1, energyMap.size(), "体力等级配置不连续(Level必须从1级开始连续递增)");
    }

}
