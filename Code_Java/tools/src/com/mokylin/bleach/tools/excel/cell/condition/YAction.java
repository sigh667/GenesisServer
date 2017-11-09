package com.mokylin.bleach.tools.excel.cell.condition;

import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * y定义的解析。
 * 
 * @author pangchong
 *
 */
final class YAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.y = Boolean.parseBoolean(aMatch);
		if(condition.x) condition.importPackage.add(TemplateConfigException.class.getName());
	}

}
