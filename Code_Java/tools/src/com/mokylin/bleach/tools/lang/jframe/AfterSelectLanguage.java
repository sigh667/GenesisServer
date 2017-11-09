package com.mokylin.bleach.tools.lang.jframe;

import com.mokylin.bleach.tools.lang.Language;

/**
 * 选择语言之后
 * @author yaguang.xiao
 *
 */
public interface AfterSelectLanguage {

	/**
	 * 选择语言之后的操作
	 * @param lan
	 */
	void action(Language lan);
}
