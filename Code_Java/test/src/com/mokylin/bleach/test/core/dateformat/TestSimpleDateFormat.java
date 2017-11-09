package com.mokylin.bleach.test.core.dateformat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSimpleDateFormat {

	private static SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:MM:ss");
	
	public static void main(String[] args) {
		
		final int count = 10000000;
		
		long now = System.currentTimeMillis();
		long start = System.nanoTime();
		
		for(int i = 0;i < count;i ++) {
			formatter.format(new Date(now));
			now += 1000;
		}
		
		long duration = System.nanoTime() - start;
		
		System.out.println(duration);
		System.out.println(duration / count);
	}

}
