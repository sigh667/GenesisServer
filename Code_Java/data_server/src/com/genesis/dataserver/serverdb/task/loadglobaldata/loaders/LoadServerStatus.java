package com.genesis.dataserver.serverdb.task.loadglobaldata.loaders;

import com.genesis.dataserver.globals.Globals;
import com.genesis.dataserver.serverdb.ServerDBManager;
import com.genesis.dataserver.serverdb.task.loadglobaldata.ILoadGlobalData;
import com.genesis.core.orm.hibernate.HibernateDBService;
import com.genesis.core.redis.IRedis;
import com.genesis.gamedb.orm.entity.ServerStatusEntity;

public class LoadServerStatus implements ILoadGlobalData {

    @Override
    public void load(ServerDBManager dbm) {
        if (dbm.getCurrentServerId() != dbm.getOriginalServerId()) {
            //合过服的羊服就不用加载此信息了
            return;
        }

        //1.0.1 获取服务器状态数据
        HibernateDBService dbservice = Globals.getDbservice();
        ServerStatusEntity serverStatus =
                dbservice.getById(ServerStatusEntity.class, dbm.getCurrentServerId());

        IRedis redis = dbm.getiRedis();
        if (serverStatus != null) {
            redis.getValueOp()
                    .set(serverStatus.newRedisKey(dbm.getCurrentServerId()).getKey(), serverStatus);
        }
    }

}
