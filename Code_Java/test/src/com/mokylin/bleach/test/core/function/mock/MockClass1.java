package com.mokylin.bleach.test.core.function.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockClass1 {
	
	private final int a = 0;
	
	private final long b = 1L;
	
	private final String c = "";
	
	private static Logger logger = LoggerFactory.getLogger(MockClass1.class);

	public int getA() {
		return a;
	}

	public Long getB() {
		return b;
	}

	public String getC() {
		return c;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MockClass1.logger = logger;
	}
}
