package com.mokylin.bleach.test.core.serviceinit.service;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service2 implements ServiceInitializeRequired {

	private static Service2 Instance2 = new Service2();
	
	private Service2() {}
	
	public static Service2 service() {
		return Instance2;
	}
	
	@Override
	public void init() {
		Statistics.service2Inited = true;
	}

}
