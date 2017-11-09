package com.mokylin.bleach.dataserver.serverdb.task;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.gamedb.human.HumanData;
import com.mokylin.bleach.gamedb.orm.entity.FunctionEntity;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gamedb.orm.entity.ItemEntity;
import com.mokylin.bleach.gamedb.orm.entity.HeroEntity;
import com.mokylin.bleach.gamedb.orm.entity.ShopEntity;
import com.mokylin.bleach.gamedb.redis.key.model.FunctionKey;
import com.mokylin.bleach.gamedb.redis.key.model.HumanKey;
import com.mokylin.bleach.gamedb.redis.key.model.ItemKey;
import com.mokylin.bleach.gamedb.redis.key.model.HeroKey;
import com.mokylin.bleach.gamedb.redis.key.model.ShopKey;
import com.mokylin.bleach.servermsg.dataserver.LoadHumanDataMessage;
import com.mokylin.bleach.servermsg.gameserver.HumanDataMsg;

public class LoadHumanDataTask implements Runnable {

	private final LoadHumanDataMessage msg;
	private final IRemote sender;
	private final IRedis iRedis;
	
	public LoadHumanDataTask(LoadHumanDataMessage msg, IRemote sender, IRedis iRedis) {
		this.msg = msg;
		this.sender = sender;
		this.iRedis = iRedis;
	}

	@Override
	public void run() {
		HibernateDBService dbservice = Globals.getDbservice();
		
		//从数据库中加载角色所有信息
		final HumanData humanData = new HumanData(sender.getServerId(),msg.humanId);
		final HumanDataMsg msgToSend = new HumanDataMsg(msg.timeoutId, humanData, msg.accountId, msg.channel);
		humanData.humanEntity = dbservice.getById(HumanEntity.class, msg.humanId);
		humanData.itemEntityList = dbservice.findByNamedQueryAndNamedParamAllT(ItemEntity.class, "queryItem",
				new String[] { "humanId" }, new Object[] { msg.humanId });
		humanData.heroEntityList = dbservice.findByNamedQueryAndNamedParamAllT(HeroEntity.class, "queryHero",
				new String[] { "humanId" }, new Object[] { msg.humanId });
		humanData.shopEntityList = dbservice.findByNamedQueryAndNamedParamAllT(ShopEntity.class, "queryShop",
				new String[] { "humanId" }, new Object[] { msg.humanId });
		humanData.functionEntityList = dbservice.findByNamedQueryAndNamedParamAllT(FunctionEntity.class, "queryFunction", 
				new String[] { "humanId" }, new Object[] { msg.humanId });
		
		//将其存入Redis中
		IPipelineOp pipeLine = iRedis.pipeline();
		pipeLine.exec(new PipelineProcess() {
			
			@Override
			public void apply() {
				
				//除HumanEntity外的所有Entity都要判null
				HumanKey humanKey = humanData.humanEntity.newRedisKey(sender.getServerId());
				humanKey.getEntityRedisOp().flushToRedis(this, humanKey, humanData.humanEntity);
				
				if (humanData.itemEntityList!=null && !humanData.itemEntityList.isEmpty()) {
					for (ItemEntity itemEntity : humanData.itemEntityList) {
						ItemKey itemKey = itemEntity.newRedisKey(sender.getServerId());
						itemKey.getEntityRedisOp().flushToRedis(this, itemKey, itemEntity);
					}
				}
				
				if (humanData.heroEntityList!=null && !humanData.heroEntityList.isEmpty()) {
					for (HeroEntity heroEntity : humanData.heroEntityList) {
						HeroKey heroKey = heroEntity.newRedisKey(sender.getServerId());
						heroKey.getEntityRedisOp().flushToRedis(this, heroKey, heroEntity);
					}
				}
				
				if (humanData.shopEntityList != null && !humanData.shopEntityList.isEmpty()) {
					for (ShopEntity shopEntity : humanData.shopEntityList) {
						ShopKey shopKey = shopEntity.newRedisKey(sender.getServerId());
						shopKey.getEntityRedisOp().flushToRedis(this, shopKey, shopEntity);
					}
				}
				
				if (humanData.functionEntityList != null && !humanData.functionEntityList.isEmpty()) {
					for (FunctionEntity functionEntity : humanData.functionEntityList) {
						FunctionKey functionKey = functionEntity.newRedisKey(sender.getServerId());
						functionKey.getEntityRedisOp().flushToRedis(this, functionKey, functionEntity);
					}
				}
			}
		});
		
		sender.sendMessage(msgToSend);
	}

}
