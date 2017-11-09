package com.mokylin.td.network2client.core.id;

import java.util.concurrent.atomic.AtomicLong;

import com.mokylin.bleach.core.annotation.ThreadSafe;

/**
 * 自增Id生成器
 * @author baoliang.shen
 *
 */
@ThreadSafe
public class IdGenerator {

	/** 开始Id */
	private static final long START_ID = 0L;
	/** 当前的id */
	private static AtomicLong id = new AtomicLong(START_ID);

	/**
	 * 获取下一个id
	 * 
	 * @return
	 */
	public static long getNextId() {
		return id.incrementAndGet();
	}
}
