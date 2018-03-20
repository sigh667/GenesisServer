package com.genesis.dataserver.serverdb.task;

import com.genesis.dataserver.serverdb.ServerDBManager;

public class CheckDataCacheTask implements Runnable {

    private ServerDBManager dbm;

    public CheckDataCacheTask(ServerDBManager dbm) {
        this.dbm = dbm;
    }

    @Override
    public void run() {
        if (dbm.getDataCache().isNeedFlush()) {
            dbm.getDataCache().flush();
        }
    }

}
