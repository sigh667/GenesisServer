package com.mokylin.bleach.test.core.serviceinit.servicefilter;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service13 implements ServiceInitializeRequired {

	private static Service13 Instance2 = new Service13();
	
	private Service13() {}
	
	public static Service13 service() {
		return Instance2;
	}
	
	@Override
	public void init() {
		StatisticsFilter.service13Inited = true;
	}

}
