package com.mokylin.bleach.test.core.serviceinit.servicedep;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service5 implements ServiceInitializeRequired {

	private static Service5 Instance = new Service5();
	
	private Service5() {}
	
	public static Service5 service() {
		return Instance;
	}
	
	@Override
	public void init() {
		StatisticsDep.initService5();
	}

}
