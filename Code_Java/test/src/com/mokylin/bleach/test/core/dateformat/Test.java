package com.mokylin.bleach.test.core.dateformat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Test {

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:MM:ss");
	
	public static void main(String[] args) {
		
		final int count = 10000000;
		
		long now = System.currentTimeMillis();
		long start = System.nanoTime();
		
		for(int i = 0;i < count;i ++) {
			formatter.print(now);
			now += 1000;
		}
		
		long duration = System.nanoTime() - start;
		
		System.out.println(duration);
		System.out.println(duration / count);
	}

}
