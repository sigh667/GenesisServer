package com.genesis.test.core.serviceinit.servicedep;

import com.genesis.core.serviceinit.Depend;
import com.genesis.core.serviceinit.ServiceInitializeRequired;

@Depend({Service5.class, Service6.class})
public class Service4 implements ServiceInitializeRequired {

    private static Service4 Instance = new Service4();

    private Service4() {
    }

    public static Service4 service() {
        return Instance;
    }

    @Override
    public void init() {
        StatisticsDep.initService4();
    }

}
