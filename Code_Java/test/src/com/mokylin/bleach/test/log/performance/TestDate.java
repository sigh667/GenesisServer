package com.mokylin.bleach.test.log.performance;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:MM:ss");
		String dateStr = dateFormat.format(date);
		System.out.println(dateStr);
	}

}
