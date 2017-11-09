package com.mokylin.bleach.tools.excel.cell.condition;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

/**
 * 用于解析模板文件中定义的每一个cell（每一行）的condition的类。<p>
 * 
 * 对应模板的一行：String areaName;[notNull=true;lua=true;lang=true] 中[]内的信息。
 * 
 * @author pangchong
 *
 */
public final class CellConditionPaser {

	public static Condition parse(String conditionText){
		if(StringUtils.isEmpty(conditionText)) return new Condition();
		
		Condition condition = new Condition();
		for (String _c : Splitter.on(";").split(conditionText)) {
			_c = _c.trim().toLowerCase();
			CellConditionPattern.match(_c, condition);
		}
		return condition;
	}
}
