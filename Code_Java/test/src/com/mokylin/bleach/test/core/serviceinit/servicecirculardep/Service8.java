package com.mokylin.bleach.test.core.serviceinit.servicecirculardep;

import com.genesis.core.serviceinit.Depend;
import com.genesis.core.serviceinit.ServiceInitializeRequired;

@Depend({Service9.class})
public class Service8 implements ServiceInitializeRequired {

    private static Service8 Instance = new Service8();

    private Service8() {
    }

    public static Service8 service() {
        return Instance;
    }

    @Override
    public void init() {

    }

}
