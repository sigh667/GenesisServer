package com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata.loaders;

import java.util.Iterator;
import java.util.List;

import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;
import com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata.ILoadGlobalData;
import com.mokylin.bleach.gamedb.orm.entity.ShopDiscountEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ShopDiscountKey;

/**
 * 加载物品打折数据
 * @author yaguang.xiao
 *
 */
public class LoadShopDiscount implements ILoadGlobalData {

	@Override
	public void load(final ServerDBManager dbm) {
		if (dbm.getCurrentServerId()!=dbm.getOriginalServerId()) {
			//合过服的羊服就不用加载此信息了
			return;
		}
		
		HibernateDBService dbservice = Globals.getDbservice();
		
		final List<ShopDiscountEntity> shopDiscountEntities = dbservice.findByNamedQueryAndNamedParamAllT(ShopDiscountEntity.class, "queryShopDiscount",
				new String[] { "serverId" }, new Object[] { dbm.getCurrentServerId() });
		if(shopDiscountEntities == null || shopDiscountEntities.isEmpty()) {
			return;
		}
		
		for(Iterator<ShopDiscountEntity> it = shopDiscountEntities.iterator(); it.hasNext();) {
			ShopDiscountEntity entity = it.next();
			if(entity == null) {
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
