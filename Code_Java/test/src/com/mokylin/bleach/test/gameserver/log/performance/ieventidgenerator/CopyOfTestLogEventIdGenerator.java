package com.mokylin.bleach.test.gameserver.log.performance.ieventidgenerator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mokylin.bleach.gameserver.core.log.LogEventIdGenerator;

public class CopyOfTestLogEventIdGenerator {

	public static void main(String[] args) {
		final int threadNum = Runtime.getRuntime().availableProcessors() + 2;
		final int loopTime = 1000000;
		final CountDownLatch startCountDown = new CountDownLatch(1);
		final CountDownLatch endCountDown = new CountDownLatch(threadNum);
		final LogEventIdGenerator logEventIdGenerator = new LogEventIdGenerator();
		Runnable task = new Runnable() {

			@Override
			public void run() {
				try {
					startCountDown.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				for(int i = 0;i < loopTime;i ++) {
					logEventIdGenerator.generateEventId();
				}
				
				endCountDown.countDown();
			}

		};
		
		
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		for(int i = 0;i < threadNum;i ++) {
			executorService.submit(task);
		}
		
		long start = System.currentTimeMillis();
		startCountDown.countDown();
		try {
			endCountDown.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		
		System.out.println(end - start);
		
		executorService.shutdown();
	}

}
