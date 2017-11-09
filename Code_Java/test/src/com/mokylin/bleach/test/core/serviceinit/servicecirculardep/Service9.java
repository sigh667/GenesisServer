package com.mokylin.bleach.test.core.serviceinit.servicecirculardep;

import com.mokylin.bleach.core.serviceinit.Depend;
import com.mokylin.bleach.core.serviceinit.ServiceInitializeRequired;

@Depend( { Service7.class } )
public class Service9 implements ServiceInitializeRequired {

	private static Service9 Instance = new Service9();
	
	private Service9() {}
	
	public static Service9 service() {
		return Instance;
	}
	
	@Override
	public void init() {
		
	}

}
