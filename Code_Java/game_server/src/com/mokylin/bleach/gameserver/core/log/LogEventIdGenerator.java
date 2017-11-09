package com.mokylin.bleach.gameserver.core.log;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 日志iEventId生成器
 * @author yaguang.xiao
 *
 */
public class LogEventIdGenerator {
	
	/** 事件Id的长度 */
	private static final int EVENT_ID_LENGTH = 16;
	/** 随机字符范围(33-126，不包括124) */
	private static final char[] chars = new char[93];
	static {
		int j = 0;
		for(int i = 33;i <= 126;i ++) {
			if(i == 124) continue; // 124代表|,日志的字段内容里面不能出现|
			
			chars[j] = (char) i;
			j ++;
		}
	}
	
	private Random random = new Random();
	
	/**
	 * 生成事件Id
	 * @return
	 */
	public String generateEventId() {
		return RandomStringUtils.random(EVENT_ID_LENGTH, 0, 0, false, false, chars, random);
	}

}
