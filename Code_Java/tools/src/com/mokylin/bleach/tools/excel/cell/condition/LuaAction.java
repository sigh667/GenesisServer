package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * lua定义的解析。
 * 
 * @author pangchong
 *
 */
final class LuaAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.lua = Boolean.parseBoolean(aMatch);
	}

}
