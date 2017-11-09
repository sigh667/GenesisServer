package com.mokylin.bleach.test.core.serviceinit.servicefilter;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service16 implements ServiceInitializeRequired {

	private static Service16 Instance = new Service16();
	
	private Service16() {}
	
	public static Service16 service() {
		return Instance;
	}
	
	@Override
	public void init() {
		StatisticsFilter.service16Inited = true;
	}

}
