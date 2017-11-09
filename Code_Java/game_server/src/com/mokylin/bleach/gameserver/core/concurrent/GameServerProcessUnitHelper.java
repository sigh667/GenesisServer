package com.mokylin.bleach.gameserver.core.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import akka.dispatch.ExecutionContexts;

import com.mokylin.bleach.core.akka.Akka;

/**
 * 创建GameProcessUnit的帮助类。
 * 
 * @author pangchong
 *
 */
public class GameServerProcessUnitHelper {
	
	/**
	 * 创建一个单线程的GameProcessUnit，并指定线程名为threadName。
	 * 
	 * @param threadName
	 * @return
	 */
	public static GameProcessUnit createSingleProcessUnit(final String threadName){
		checkNotNull(threadName, "Can not create a Single Process Unit named NULL!");
		return new GameProcessUnit(threadName, ExecutionContexts.fromExecutorService(Executors.newSingleThreadExecutor(new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadName);
			}
		})));
	}
	
	/**
	 * 创建一个名字为threadName并且线程数为threadNum的GameProcessUnit。
	 * 
	 * @param threadName
	 * @param threadNum
	 * @return
	 */
	public static GameProcessUnit createFixedProcessUnit(final String threadName, final int threadNum){
		checkNotNull(threadName, "Can not create a Fixed Process Unit named NULL!");
		return new GameProcessUnit(threadName, ExecutionContexts.fromExecutorService(Executors.newFixedThreadPool(threadNum, new ThreadFactory() {			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadName);
			}
		})));
	}
	
	/**
	 * 创建一个名字为threadName，线程数为threadNum的GameScheduledProcessUnit。
	 * @param threadName
	 * @param threadNum
	 * @param akka
	 * @return
	 */
	public static GameScheduledProcessUnit createScheduleProcessUnit(final String threadName, final int threadNum, Akka akka){
		return new GameScheduledProcessUnit(threadName, ExecutionContexts.fromExecutorService(Executors.newFixedThreadPool(threadNum)), akka);
	}
}
