package com.genesis.dataserver.serverdb.task.loadglobaldata.loaders;

import com.genesis.core.orm.hibernate.HibernateDBService;
import com.genesis.core.redis.IRedis;
import com.genesis.core.redis.op.IPipelineOp;
import com.genesis.core.redis.op.PipelineProcess;
import com.genesis.dataserver.globals.Globals;
import com.genesis.dataserver.serverdb.ServerDBManager;
import com.genesis.dataserver.serverdb.task.loadglobaldata.ILoadGlobalData;
import com.genesis.gamedb.orm.entity.ShopDiscountEntity;
import com.genesis.gamedb.redis.key.model.ShopDiscountKey;

import java.util.Iterator;
import java.util.List;

/**
 * 加载物品打折数据
 * @author yaguang.xiao
 *
 */
public class LoadShopDiscount implements ILoadGlobalData {

    @Override
    public void load(final ServerDBManager dbm) {
        if (dbm.getCurrentServerId() != dbm.getOriginalServerId()) {
            //合过服的羊服就不用加载此信息了
            return;
        }

        HibernateDBService dbservice = Globals.getDbservice();

        final List<ShopDiscountEntity> shopDiscountEntities = dbservice
                .findByNamedQueryAndNamedParamAllT(ShopDiscountEntity.class, "queryShopDiscount",
                        new String[]{"serverId"}, new Object[]{dbm.getCurrentServerId()});
        if (shopDiscountEntities == null || shopDiscountEntities.isEmpty()) {
            return;
        }

        for (Iterator<ShopDiscountEntity> it = shopDiscountEntities.iterator(); it.hasNext(); ) {
            ShopDiscountEntity entity = it.next();
            if (entity == null) {
                it.remove();
            }
        }

        IRedis redis = dbm.getiRedis();
        IPipelineOp pip = redis.pipeline();
        pip.exec(new PipelineProcess() {

            @Override
            public void apply() {
                for (ShopDiscountEntity entity : shopDiscountEntities) {
                    ShopDiscountKey redisKey = entity.newRedisKey(dbm.getCurrentServerId());
                    this.getHashOp().hset(redisKey.getKey(), redisKey.getField(), entity);
                }
            }

        });
    }

}
