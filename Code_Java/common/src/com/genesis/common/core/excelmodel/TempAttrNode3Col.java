package com.genesis.common.core.excelmodel;

import com.genesis.common.prop.battleprop.HeroBattlePropId;
import com.genesis.core.template.annotation.BeanFieldNumber;
import com.genesis.core.template.annotation.ExcelRowBinding;

@ExcelRowBinding
public class TempAttrNode3Col {

    /** 属性ID */
    @BeanFieldNumber(number = 0)
    private HeroBattlePropId attributeIndex;
    /** 属性值（绝对值） */
    @BeanFieldNumber(number = 1)
    private int absValue;
    /** 属性值（百分比） */
    @BeanFieldNumber(number = 2)
    private int perValue;

    public HeroBattlePropId getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(HeroBattlePropId attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public int getAbsValue() {
        return absValue;
    }

    public void setAbsValue(int absValue) {
        this.absValue = absValue;
    }

    public int getPerValue() {
        return perValue;
    }

    public void setPerValue(int perValue) {
        this.perValue = perValue;
    }

}
