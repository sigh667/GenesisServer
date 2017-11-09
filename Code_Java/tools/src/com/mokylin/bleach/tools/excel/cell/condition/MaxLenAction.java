package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * manLen定义的解析。
 * 
 * @author pangchong
 *
 */
final class MaxLenAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.maxLen = Integer.parseInt(aMatch);
	}

}
