package com.mokylin.bleach.core.util;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;


/**
 * 字符串操作类
 * 
 */
public class BleachStringUtil {

	/** 随机数对象 */
	private static final Random random = new Random();
	/** 数字与字母字典 */
	private static final char[] LETTER_AND_DIGIT = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	/** 数字与字母字典长度 */
	private static final int LETTER_AND_DIGIT_LENGTH = LETTER_AND_DIGIT.length;

	private BleachStringUtil() {
	}

	/**
	 * 生成固定长度的随机字符串
	 * 
	 * @param len 随机数长度
	 * @return 生成的随机数
	 */
	public static String getRandomString(final int len) {
		if (len < 1)
			return "";
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(LETTER_AND_DIGIT[random.nextInt(LETTER_AND_DIGIT_LENGTH)]);
		}
		return sb.toString();
	}

	/**
	 * 将字符串首字母大写，剩余的转成小写
	 * 
	 * @param s
	 * @return
	 */
	public static String upperCaseFirstCharOnly(String s) {
		return StringUtils.isEmpty(s) ? s : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	/**
	 * 将字符串首字母小写，其余不变
	 * 
	 * @param s
	 * @return
	 */
	public static String lowerCaseFirstChar(String s) {
		return StringUtils.isEmpty(s) ? s : s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	/**
	 * 将字符串首字母大写，其余不变
	 * 
	 * @param s
	 * @return
	 */
	public static String upperCaseFirstChar(String s) {
		return StringUtils.isEmpty(s) ? s : s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
