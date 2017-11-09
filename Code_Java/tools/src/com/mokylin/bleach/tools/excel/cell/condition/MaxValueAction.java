package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * maxValue定义的解析。
 * 
 * @author pangchong
 *
 */
final class MaxValueAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.maxValue = Integer.parseInt(aMatch);
	}

}
