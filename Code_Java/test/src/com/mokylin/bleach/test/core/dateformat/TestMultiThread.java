package com.mokylin.bleach.test.core.dateformat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TestMultiThread {

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:MM:ss");
	
	public static void main(String[] args) throws InterruptedException {

		final int threadNum = 10;
		final int count = 100000000;
		final int perThreadCount = count / threadNum;
		
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch endLatch = new CountDownLatch(threadNum);
		
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		Runnable task = new Runnable() {

			@Override
			public void run() {
				
				try {
					startLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				long now = System.currentTimeMillis();
				for(int i = 0;i < perThreadCount;i ++) {
					formatter.print(now ++);
				}
				
				endLatch.countDown();
			}
			
		};
		
		for(int i = 0;i < threadNum;i ++) {
			executor.submit(task);
		}
		
		long startTime = System.nanoTime();
		startLatch.countDown();
		endLatch.await();
		long duration = System.nanoTime() - startTime;
		
		executor.shutdown();
		
		System.out.println(duration);
		System.out.println(duration / count);
	}

}
