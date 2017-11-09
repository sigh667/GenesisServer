package com.mokylin.bleach.dataserver.serverdb.task;

import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;
import com.mokylin.bleach.gamedb.redis.DirtyDataInfo;

public class PutDirtyDataToCacheTask implements Runnable {
	
	private ServerDBManager dbm;
	private DirtyDataInfo dirtyDataInfo;

	public PutDirtyDataToCacheTask(ServerDBManager dbm, DirtyDataInfo dirtyDataInfo) {
		this.dbm = dbm;
		this.dirtyDataInfo = dirtyDataInfo;
	}

	@Override
	public void run() {
		dbm.getDataCache().put(dirtyDataInfo);
	}

}
