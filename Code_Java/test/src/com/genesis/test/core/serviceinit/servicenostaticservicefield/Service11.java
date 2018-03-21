package com.genesis.test.core.serviceinit.servicenostaticservicefield;

import com.genesis.core.serviceinit.ServiceInitializeRequired;

public class Service11 implements ServiceInitializeRequired {

    public static Service11 service = new Service11();

    @Override
    public void init() {

    }

}
