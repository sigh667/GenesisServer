package com.mokylin.bleach.test.core.serviceinit.servicenostaticservicefield;

import com.genesis.core.serviceinit.ServiceInitializeRequired;

public class Service10 implements ServiceInitializeRequired {

    public static Service11 Instance = new Service11();

    @Override
    public void init() {

    }

}
