package com.mokylin.bleach.gameserver.core.lang;

/**
 * 语言相关的常量定义
 * 
 * @author yaguang.xiao
 *
 */
public class LangConstants {

	/** 公用常量 1 ~ 10000 */
	private static Integer COMMON_BASE = 0;
	/** 标点符号 */
	@LangContent( "【" )
	public static final Integer LEFT_BRACKET = ++COMMON_BASE;
	@LangContent( "】" )
	public static final Integer RIGHT_BRACKET = ++COMMON_BASE;
	@LangContent( "。" )
	public static final Integer FULL_POINT = ++COMMON_BASE;
}
