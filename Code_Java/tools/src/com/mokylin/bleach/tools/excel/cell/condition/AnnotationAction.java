package com.mokylin.bleach.tools.excel.cell.condition;

/**
 * collection, object, nottranslate和cell的定义的解析。
 * 
 * @author pangchong
 *
 */
final class AnnotationAction implements IConditionAction {

	@Override
	public void act(String aCondition, String aMatch, Condition condition) {
		if(!"".equals(condition.annotation.toString())){
			condition.annotation.append(";");
		}
		condition.annotation.append(aCondition);
	}

}
