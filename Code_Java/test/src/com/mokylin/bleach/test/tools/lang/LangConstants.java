package com.mokylin.bleach.test.tools.lang;

import com.mokylin.bleach.gameserver.core.lang.LangContent;

public class LangConstants {

	/** 公用常量 1 ~ 10000 */
	private static Integer COMMON_BASE = 0;
	@LangContent( "。" )
	public static final Integer LEFT_BRACKET = ++COMMON_BASE;
	@LangContent( "死神" )
	public static final Integer RIGHT_BRACKET = ++COMMON_BASE;
	@LangContent( "しにがみ" )
	public static final Integer FULL_POINT = ++COMMON_BASE; 
}