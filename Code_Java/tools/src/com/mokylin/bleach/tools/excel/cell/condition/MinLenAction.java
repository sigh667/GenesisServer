package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * minLen定义的解析。
 * 
 * @author pangchong
 *
 */
final class MinLenAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.minLen = Integer.parseInt(aMatch);
	}

}
