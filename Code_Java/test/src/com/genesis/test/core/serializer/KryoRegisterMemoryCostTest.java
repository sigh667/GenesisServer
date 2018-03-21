package com.genesis.test.core.serializer;

import com.google.common.collect.Lists;

import com.genesis.core.serializer.ISerializer;
import com.genesis.core.serializer.KryoPool;

import java.util.List;

public class KryoRegisterMemoryCostTest {

    public static void main(String[] args) throws Exception {
        System.out.println("start!");
        long heapSize1 = Runtime.getRuntime().totalMemory();
        long heapMaxSize1 = Runtime.getRuntime().maxMemory();
        long heapFreeSize1 = Runtime.getRuntime().freeMemory();

        KryoPool pool = new KryoPool(new String[]{"com.mokylin.bleach"});
        List<ISerializer> serList = Lists.newArrayList();
        for (int i = 0; i < 6; i++) {
            serList.add(pool.borrow());
        }

        long heapSize2 = Runtime.getRuntime().totalMemory();
        long heapMaxSize2 = Runtime.getRuntime().maxMemory();
        long heapFreeSize2 = Runtime.getRuntime().freeMemory();

        float div = 1024 * 1024;

        System.out.println("heapSize:\t" + heapSize1 / div + "\tmaxSize:\t" + heapMaxSize1 / div +
                "\tfreeSize:\t" + heapFreeSize1 / div);
        System.out.println("heapSize:\t" + heapSize2 / div + "\tmaxSize:\t" + heapMaxSize2 / div +
                "\tfreeSize:\t" + heapFreeSize2 / div);
    }

}
