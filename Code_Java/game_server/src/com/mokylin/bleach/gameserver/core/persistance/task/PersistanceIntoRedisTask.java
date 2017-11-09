package com.mokylin.bleach.gameserver.core.persistance.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.config.Mapping;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IHumanRelatedEntity;
import com.mokylin.bleach.gamedb.orm.IServerRelatedEntity;
import com.mokylin.bleach.gamedb.redis.DbOp;
import com.mokylin.bleach.gamedb.redis.key.IRedisKey;
import com.mokylin.bleach.gamedb.redis.key.SpecialRedisKeyBuilder;
import com.mokylin.bleach.gameserver.core.concurrent.ArgsRunnable;
import com.mokylin.bleach.gameserver.core.concurrent.AsyncArgs;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.humaninfocache.HumanInfoCache;
import com.mokylin.bleach.gameserver.core.persistance.model.DirtyData;

/**
 * 将数据保存到Redis中<br>
 * 在Redis线程处理<br>
 * @author baoliang.shen
 *
 */
public class PersistanceIntoRedisTask implements ArgsRunnable {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(PersistanceIntoRedisTask.class);
	
	private final int serverId;
	
	/**需要保存的数据*/
	private final List<DirtyData> dirtyList;
	
	private final IRedis redis;

	public PersistanceIntoRedisTask(int serverId, List<DirtyData> dirtyList, IRedis redis) {
		this.serverId = serverId;
		this.dirtyList = dirtyList;
		this.redis = redis;
	}

	@Override
	public void run(AsyncArgs args) {
		IPipelineOp pipeline = redis.pipeline();
		@SuppressWarnings("unchecked")
		final HashMap<Long, Integer> human2originalServerId = (HashMap<Long, Integer>) args.get(HumanInfoCache.HUMAN_TO_ORIGINAL_SERVER_ID);
		pipeline.exec(new PipelineProcess() {

			@Override
			public void apply() {
				for (DirtyData dirtyData : dirtyList) {
					try {
						IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>> redisKey = dirtyData.getDirtyDataInfo().getRedisKey();
						DbOp dbOp = dirtyData.getDirtyDataInfo().getDbOp();

						if (dbOp==DbOp.DELETE) {
							redisKey.getEntityRedisOp().deleteFromRedis(this, redisKey);
						} else {
							@SuppressWarnings("unchecked")
							IRedisKey<? extends Serializable, EntityWithRedisKey<?>> redisKeyTmp
									= (IRedisKey<? extends Serializable, EntityWithRedisKey<?>>) redisKey;
							redisKeyTmp.getEntityRedisOp().flushToRedis(this, redisKeyTmp, dirtyData.getEntity());
						}

						int originalServerId = Mapping.INVALID_SERVER_ID;
						if (dirtyData.getEntity() instanceof IHumanRelatedEntity) {
							//Human相关的数据
							IHumanRelatedEntity iHumanRelatedEntity = (IHumanRelatedEntity)dirtyData.getEntity();
							originalServerId = getOriginalServerId(iHumanRelatedEntity.humanId());
							
							Map<Integer, Integer> gs_dbs_Map = Globals.getServerMappings().getGs_dbs_Map();
							if (!gs_dbs_Map.containsKey(originalServerId)) {
								logger.warn("originalServerId【{}】 不存在，EntityClass【{}】，humanId【{}】",
										originalServerId, dirtyData.getEntity().getClass().getName(), iHumanRelatedEntity.humanId());
								continue;
							}
						} else if (dirtyData.getEntity() instanceof IServerRelatedEntity) {
							//服务器相关的数据
							IServerRelatedEntity iServerRelatedEntity = (IServerRelatedEntity)dirtyData.getEntity();
							originalServerId = iServerRelatedEntity.serverId();
						} else {
							//默认的使用当前服务器ID。走到这里的数据，在合服时必须迁移
							originalServerId = serverId;
						}
						
						String dirtyDataKey = SpecialRedisKeyBuilder.buildDirtyDataKey(originalServerId);
						this.getListOp().rpush(dirtyDataKey, dirtyData.getDirtyDataInfo());
					} catch (Exception e) {
						logger.error("PersistanceIntoRedisTask flush to redis runnable throw Exception!", e);
						continue;
					}
				}
			}

			private int getOriginalServerId(long humanId) {
				if (human2originalServerId.containsKey(humanId)) {
					return human2originalServerId.get(humanId);
				} else {
					return Mapping.INVALID_SERVER_ID;
				}
			}
		});
		
	}

}
