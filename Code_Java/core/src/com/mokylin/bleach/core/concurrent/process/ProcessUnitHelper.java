package com.mokylin.bleach.core.concurrent.process;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ProcessUnitHelper {
	
	public static ProcessUnit createSingleProcessUnit(final String threadName){
		checkNotNull(threadName, "Can not create a Single Process Unit named NULL!");
		return new ProcessUnit(threadName, Executors.newSingleThreadExecutor(new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadName);
			}
		}));
	}
	
	public static ProcessUnit createFixedProcessUnit(final String threadName, final int threadNum){
		checkNotNull(threadName, "Can not create a Fixed Process Unit named NULL!");
		return new ProcessUnit(threadName, Executors.newFixedThreadPool(threadNum, new ThreadFactory() {			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadName);
			}
		}));
	}
	
	public static ScheduledProcessUnit createScheduledProcessUnit(final String threadName, final int threadNum){
		checkNotNull(threadName, "Can not create a Scheduled Process Unit named NULL!");
		return new ScheduledProcessUnit(threadName, Executors.newScheduledThreadPool(threadNum, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, threadName);
			}
		}));
	}
}
