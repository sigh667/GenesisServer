package com.mokylin.bleach.tools.lang;

import com.mokylin.bleach.gameserver.core.lang.LangConstants;

public class Constants {

	/** 没有添加final标示符的原因是测试代码需要修改此变量 */
	public static Class<?> LANG_CONSTANT_CLASS = LangConstants.class;
	/** 没有添加final标示符的原因是测试代码需要修改此变量 */
	public static String LANG_FILE_PATH = "..\\resources\\i18n\\";
	/** 配置文件路径 */
	public static final String CONFIG_FILE_NAME = "lang_config.xlsx";
	/** Int类型 */
	public static final String TYPE_INT = "Integer";
	/** 文字表格的名字 */
	public static final String LANG_SHEET = "Lang";
	/** 版本表格的名字 */
	public static final String VERSION_SHEET = "Version";
	/** 修改足迹文件路径 */
	public static final String MODIFY_FOOTPRINT_FILE_NAME = "modify_footprint.xlsx";
	/** 修改足迹表格的名字 */
	public static final String CHANGE_FOOTPRINT = "Change_Footprint";
}
