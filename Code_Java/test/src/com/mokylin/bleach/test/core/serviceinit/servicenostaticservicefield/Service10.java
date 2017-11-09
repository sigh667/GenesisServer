package com.mokylin.bleach.test.core.serviceinit.servicenostaticservicefield;

import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

public class Service10 implements ServiceInitializeRequired {

	public static Service11 Instance = new Service11();
	
	@Override
	public void init() {
		
	}

}
