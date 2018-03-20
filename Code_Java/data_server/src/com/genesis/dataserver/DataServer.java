package com.genesis.dataserver;

import com.genesis.dataserver.globals.Globals;

/**
 * DataServer程序入口
 * @author Joey
 *
 */
public class DataServer {

    public static void main(String[] args) {
        try {
            Globals.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
