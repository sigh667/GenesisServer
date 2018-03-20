package com.genesis.dataserver.serverdb.task;

import com.genesis.dataserver.serverdb.ServerDBManager;

public class LoadActiveHumanDataTask implements Runnable {

    private ServerDBManager dbm;

    public LoadActiveHumanDataTask(ServerDBManager serverDBManager) {
        this.dbm = serverDBManager;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        dbm.getiRedis();
    }

}
