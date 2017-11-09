package com.mokylin.bleach.test.core.serviceinit.service;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service3 implements ServiceInitializeRequired {

	private static Service3 Instance3 = new Service3();
	
	private Service3() {}
	
	public static Service3 service() {
		return Instance3;
	}
	
	@Override
	public void init() {
		Statistics.service3Inited = true;
	}

}
