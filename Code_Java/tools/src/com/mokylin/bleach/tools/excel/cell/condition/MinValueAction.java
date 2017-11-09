package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * minValue定义的解析。
 * 
 * @author pangchong
 *
 */
final class MinValueAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.minValue = Integer.parseInt(aMatch);
	}

}
