package com.mokylin.bleach.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThread {

	private static ExecutorService[] threadArray = new ExecutorService[10];
	//private ExecutorService readThreadExecutor = Executors.newSingleThreadExecutor();
	
	public static void main(String[] args) {
		for (int i = 0; i < threadArray.length; i++) {
			threadArray[i] = Executors.newSingleThreadExecutor();
			threadArray[i].submit(new SleepRunnable(threadArray[i]));
		}
		
		
//		// TODO Auto-generated method stub
//		while (true) {
////			try {
////				Thread.yield();
////				Thread.sleep(0);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
//			Thread.yield();
//		}
	}

}
