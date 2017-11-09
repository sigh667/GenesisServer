package com.mokylin.bleach.test.gameserver.log.performance;

import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mokylin.bleach.gameserver.core.log.disruptor.sendlog.SendLogManager;

public class TestLogService {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
//		System.out.println("targetHost");
//		String targetHost = sc.nextLine();
		String targetHost = "168.168.8.123";
//		System.out.println("targetPort");
//		int targetPort = sc.nextInt();
		int targetPort = 1463;
//		System.out.println("timeGap");
		long timeGap = 1000;
//		System.out.println("maxNum");
		long maxNum = 5000;
//		System.out.println("timeAccuracy");
		long timeAccuracy = 1000;
//		System.out.println("log number per second");
		final long logNumPerSecond = 1000000;
		System.out.println("start minite");
		final int startMinite = sc.nextInt();
		
		Calendar calendar = Calendar.getInstance();
		while(calendar.get(Calendar.MINUTE) != startMinite) {
			calendar.setTimeInMillis(System.currentTimeMillis());
		}
		
		SendLogManager sendLogManager = new SendLogManager(targetHost, targetPort, timeGap, maxNum, timeAccuracy);
		
//		System.out.println("threadNum");
//		int threadNum = sc.nextInt();
//		System.out.println("logCreateTimeGap");
//		long logCreateTimeGap = sc.nextLong();
		
//		final LogService logService = new LogService(new ServerGlobals(), sendLogManager);
//		for(int i = 0;i < 1000;i ++) {
//			logService.logCreateRole(1, 1, System.currentTimeMillis(), "127.0.0.1", 1, "saber", 1, 1, 1);
//			System.out.println("logged message");
//		}
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				for(int i = 0;i < logNumPerSecond;i ++) {
//					logService.logCreateRole(1354321354313543514l, 135431354321354354l, System.currentTimeMillis(), "127.0.0.1", 1354321354321352544l, "saber", 213412, 412322, 34432345);
				}
			}
			
		}, 0, 1, TimeUnit.MINUTES);
	}
	
	public static class TaskThread {
		private final Runnable task;
		private final ExecutorService executor;
		
		public TaskThread(Runnable task, ExecutorService executor) {
			this.task = task;
			this.executor = executor;
		}
		
		public void execute() {
			this.executor.execute(task);
		}
	}
}
