package com.mokylin.bleach.test.thread;

import java.util.concurrent.ExecutorService;

public class SleepRunnable implements Runnable {
	
	ExecutorService thread = null;

	public SleepRunnable(ExecutorService executorService) {
		thread = executorService;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1);
			
			thread.submit(new SleepRunnable(thread));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Thread.yield();
//		thread.submit(new SleepRunnable(thread));
	}

}
