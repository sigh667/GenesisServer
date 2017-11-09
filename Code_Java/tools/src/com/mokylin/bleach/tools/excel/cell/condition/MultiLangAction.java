package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * lang定义（标志是否多语言）的解析
 * 
 * @author pangchong
 *
 */
final class MultiLangAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		condition.lang = Boolean.parseBoolean(aMatch);
	}

}
