package com.mokylin.bleach.core.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * 线程命名
 * @author baoliang.shen
 *
 */
public class ThreadNamingFactory implements ThreadFactory {
	/** 线程名称 */
	private String _threadName;

	/**
	 * 类参数构造器
	 * 
	 * @param threadName 
	 * 
	 */
	public ThreadNamingFactory(String threadName) {
		this._threadName = threadName;
	}

	@Override
	public Thread newThread(Runnable r) {
		if (r == null) {
			return null;
		}

		return new Thread(r, this._threadName);
	}
}
