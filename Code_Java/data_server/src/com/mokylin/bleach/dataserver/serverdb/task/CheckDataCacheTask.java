package com.mokylin.bleach.dataserver.serverdb.task;

import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;

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
