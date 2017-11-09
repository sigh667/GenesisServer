package com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata.loaders;

import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;
import com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata.ILoadGlobalData;
import com.mokylin.bleach.gamedb.orm.entity.ServerStatusEntity;

public class LoadServerStatus implements ILoadGlobalData {

	@Override
	public void load(ServerDBManager dbm) {
		if (dbm.getCurrentServerId()!=dbm.getOriginalServerId()) {
			//合过服的羊服就不用加载此信息了
			return;
		}
		
		//1.0.1 获取服务器状态数据
		HibernateDBService dbservice = Globals.getDbservice();
		ServerStatusEntity serverStatus = dbservice.getById(ServerStatusEntity.class, dbm.getCurrentServerId());
		
		IRedis redis = dbm.getiRedis();
		if (serverStatus != null) {
			redis.getValueOp().set(serverStatus.newRedisKey(dbm.getCurrentServerId()).getKey(), serverStatus);
		}
	}

}
