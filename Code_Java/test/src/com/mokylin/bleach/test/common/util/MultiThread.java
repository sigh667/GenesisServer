package com.mokylin.bleach.test.common.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.mokylin.bleach.core.function.Function0;

/**
 * 用于测试的多线程工具。
 * 
 * @author pangchong
 *
 */
public enum MultiThread {
	
	UTIL;
	
	private ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, "MultiThread Test Util");
			return t;
		}
	});
	
	public void shutDown(){
		if(executor.isShutdown()) return;
		
		executor.shutdownNow();
	}

	/**
	 * 异步执行一个函数，并且等待其执行完毕。
	 * 
	 * @param function
	 * @return
	 */
	public <Return> Return asyncExecuteForDone(final Function0<Return> function){
		Future<Return> future = executor.submit(new Callable<Return>() {
			@Override
			public Return call() throws Exception {
				return function.apply();
			}
		});
		
		try{
			return future.get();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public <Return> Future<Return> asyncExecute(final Function0<Return> function){
		return executor.submit(new Callable<Return>() {
			@Override
			public Return call() throws Exception {
				return function.apply();
			}
		});
	}
}
