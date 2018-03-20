package com.genesis.common.human.template;

import com.genesis.common.core.GlobalData;
import com.genesis.core.template.annotation.ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class EnergyTemplate extends EnergyTemplateVO {

    @Override
    public void check() throws TemplateConfigException {
        int maxEnergy = GlobalData.getConstants().getMaxEnergy();
        //等级对应体力值不能大于全局体力上限
        if (this.getEnergy() > maxEnergy) {
            throwTemplateException(
                    String.format("等级对应体力上限 【%d】必须 <= 全局体力上限【%d】", this.getEnergy(), maxEnergy));
        }

    }

}
