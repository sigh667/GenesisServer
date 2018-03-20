package com.genesis.common.human.template;

import com.genesis.core.template.annotation.ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class ExpTemplate extends ExpTemplateVO {

    public ExpTemplate() {
    }

    public ExpTemplate(long humanExp, long heroExp) {
        this.humanExp = humanExp;
        this.heroExp = heroExp;
    }

    @Override
    public void check() throws TemplateConfigException {
        // TODO Auto-generated method stub

    }

    public long getExp(ExpEnum e) {
        return ExpEnum.HUMAN == e ? getHumanExp() : getHeroExp();
    }

    public static enum ExpEnum {
        HUMAN, HERO
    }
}
