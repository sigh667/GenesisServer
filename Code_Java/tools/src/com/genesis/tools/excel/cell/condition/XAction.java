package com.genesis.tools.excel.cell.condition;

import com.genesis.core.template.exception.TemplateConfigException;

/**
 * x定义的解析。
 *
 * @author pangchong
 *
 */
final class XAction implements IConditionAction {

    @Override
    public void act(String aCondition, String aMatch, Condition condition) {
        condition.x = Boolean.parseBoolean(aMatch);
        if (condition.x) {
            condition.importPackage.add(TemplateConfigException.class.getName());
        }
    }

}
