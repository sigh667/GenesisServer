package com.mokylin.bleach.test.core.serviceinit.servicenostaticservicefield;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service11 implements ServiceInitializeRequired {

	public static Service11 service = new Service11();
	
	@Override
	public void init() {
		
	}

}
