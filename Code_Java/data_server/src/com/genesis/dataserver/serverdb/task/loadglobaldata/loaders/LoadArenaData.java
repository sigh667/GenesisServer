package com.genesis.dataserver.serverdb.task.loadglobaldata.loaders;

import com.genesis.dataserver.globals.Globals;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.genesis.dataserver.serverdb.ServerDBManager;
import com.genesis.dataserver.serverdb.task.loadglobaldata.ILoadHumanRelatedGlobalData;
import com.mokylin.bleach.gamedb.orm.entity.ArenaSnapEntity;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ArenaSnapKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 加载竞技场数据
 * @author yaguang.xiao
 *
 */
public class LoadArenaData implements ILoadHumanRelatedGlobalData {

    @Override
    public void load(final ServerDBManager dbm, List<HumanEntity> humanList) {
        HibernateDBService dbservice = Globals.getDbservice();

        //组装humanId列表
        List<Long> humanIdList = new ArrayList<Long>();
        for (HumanEntity humanEntity : humanList) {
            humanIdList.add(humanEntity.getId());
        }

        //1.0开始处理各种全局数据
        //1.1竞技场
        final List<ArenaSnapEntity> arenaSnapList =
                dbservice.getByIdBatch(ArenaSnapEntity.class, humanIdList);
        for (Iterator<ArenaSnapEntity> iterator = arenaSnapList.iterator(); iterator.hasNext(); ) {
            ArenaSnapEntity arenaSnapEntity = iterator.next();
            if (arenaSnapEntity == null) {
                iterator.remove();
                continue;
            }
        }
        IRedis redis = dbm.getiRedis();
        IPipelineOp pip = redis.pipeline();
        pip.exec(new PipelineProcess() {

            @Override
            public void apply() {
                for (ArenaSnapEntity arenaSnapEntity : arenaSnapList) {
                    ArenaSnapKey redisKey = arenaSnapEntity.newRedisKey(dbm.getCurrentServerId());
                    this.getHashOp().hset(redisKey.getKey(), redisKey.getField(), arenaSnapEntity);
                }
            }

        });
    }

}
