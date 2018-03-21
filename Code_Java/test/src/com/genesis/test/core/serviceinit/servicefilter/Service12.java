package com.genesis.test.core.serviceinit.servicefilter;

import com.genesis.core.serviceinit.Depend;
import com.genesis.core.serviceinit.ServiceInitializeRequired;

@Filter
@Depend({Service13.class, Service14.class})
public class Service12 implements ServiceInitializeRequired {

    private static Service12 Instance1 = new Service12();

    private Service12() {
    }

    public static Service12 service() {
        return Instance1;
    }

    @Override
    public void init() {
        StatisticsFilter.service12Inited = true;
    }

}
