package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * notNull定义的解析。
 * 
 * @author pangchong
 *
 */
final class NotNullAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.notNull = Boolean.parseBoolean(aMatch);
	}

}
