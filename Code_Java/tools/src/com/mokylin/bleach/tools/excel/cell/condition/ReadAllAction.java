package com.mokylin.bleach.tools.excel.cell.condition;

final public class ReadAllAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.readAll = Boolean.parseBoolean(aMatch);
	}

}
