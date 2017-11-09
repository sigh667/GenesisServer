package com.mokylin.bleach.test.core.serviceinit.service;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service1 implements ServiceInitializeRequired {

	private static Service1 Instance1 = new Service1();
	
	private Service1() {}
	
	public static Service1 service() {
		return Instance1;
	}
	
	@Override
	public void init() {
		Statistics.service1Inited = true;
	}

}
