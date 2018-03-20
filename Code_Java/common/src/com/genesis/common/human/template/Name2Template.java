package com.genesis.common.human.template;

import com.genesis.core.template.annotation.ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;

@ExcelRowBinding
public class Name2Template extends Name2TemplateVO {

    @Override
    public void check() throws TemplateConfigException {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return this.name;
    }
}
