package com.mokylin.bleach.core.concurrent.fixthreadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.concurrent.ThreadNamingFactory;

/**
 * 针对绑定ID的任务的固定线程池
 * @author baoliang.shen
 *
 */
public class FixThreadPool {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(FixThreadPool.class);
	/**固定大小的线程池*/
	private final ExecutorService[] pool;
	/**发生异常时的处理方法*/
	private final IActionOnException action;
	
	public FixThreadPool(int threadCount, IActionOnException action) {
		pool = new ExecutorService[threadCount];
		for (int i = 0; i < pool.length; i++) {
			pool[i] = Executors.newSingleThreadExecutor(new ThreadNamingFactory("Login work thread->"+ i +"号"));
		}
		this.action = action;
	}
	
	public void submit(final IRunnableBindId runnable) {
		long bindId = runnable.bindId();
		bindId = bindId < 0 ? -1*bindId : bindId;
		final int index = (int)(bindId % pool.length);
		ExecutorService executor = pool[index];
		
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
					// 保证线程池的健壮性
					try {
						logger.error("FixThreadPool index[" + index + "] cause Exception", e);
						if (action!=null) {
							action.action(runnable);
						}
					} catch (Exception e1) {
						logger.error("处理异常的时候，又出了异常，蛋疼！！！", e1);
					}
				}
			}
		});
	}
}
