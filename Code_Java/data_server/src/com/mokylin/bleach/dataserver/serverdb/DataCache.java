package com.mokylin.bleach.dataserver.serverdb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.orm.BaseEntity;
import com.mokylin.bleach.core.orm.DataAccessException;
import com.mokylin.bleach.core.orm.hibernate.EntityToDelete;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.redis.op.IPipelineOp;
import com.mokylin.bleach.core.redis.op.PipelineProcess;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.redis.DbOp;
import com.mokylin.bleach.gamedb.redis.DirtyDataInfo;
import com.mokylin.bleach.gamedb.redis.key.IRedisKey;

/**
 * 脏数据缓冲
 * <p>每个原始服务器拥有一个
 * @author baoliang.shen
 *
 */
public class DataCache {
	
	/** 日志 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ServerDBManager dbm;
	
	private HashMap<IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>>,DbOp> map = new HashMap<>();
	
	/**记录最老的那个缓存数据进入的时间点*/
	private long updateTime;
	
	//满足下列条件之一，就应该将缓存数据刷到数据库了
	/**数据量*/
	final private int maxSizeToUpdate = 10000;
	/**时间*/
	final private long maxTimeMillisToUpdate = 10*1000;
	
	public DataCache(ServerDBManager dbm) {
		this.dbm = dbm;
	}

	public void put(DirtyDataInfo dirtyDataInfo) {
		
		if (dirtyDataInfo==null) {
			logger.warn("DataChche:put(DirtyDataInfo dirtyDataInfo) dirtyDataInfo is null!");
			return;
		}
		
		if (map.isEmpty()) {
			updateTime = System.currentTimeMillis();
		}
		
		IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>> redisKey = dirtyDataInfo.getRedisKey();
		DbOp operateType = dirtyDataInfo.getDbOp();
		if (redisKey==null) {
			logger.warn("DataChche:put(DirtyDataInfo dirtyDataInfo) dirtyDataInfo.getRedisKey() is null!");
			return;
		}
		if (operateType==null) {
			logger.warn("DataChche:put(DirtyDataInfo dirtyDataInfo) dirtyDataInfo.getDbOp() is null!");
			return;
		}

		if (!map.containsKey(redisKey)) {
			map.put(redisKey, operateType);
			return;
		}
		
		DbOp oldDbOp = map.get(redisKey);
		DbOp resultDbOp = oldDbOp.merge(operateType);
		map.put(redisKey, resultDbOp);
		
		if (isNeedFlush()) {
			flush();
		}
	}

	/**
	 * 是否需要立刻将缓存数据刷新到MySql
	 * @return
	 */
	public boolean isNeedFlush() {
		if (map.isEmpty())
			return false;
		if (map.size() >= maxSizeToUpdate)
			return true;
		
		long now = System.currentTimeMillis();
		if (now-updateTime >= maxTimeMillisToUpdate)
			return true;
		
		return false;
	}

	/**
	 * 将缓存数据刷新到MySql
	 */
	public void flush() {
		
		HibernateDBService dbservice = Globals.getDbservice();
		
		//1.0处理要删除的数据
		flushDeleteEntity(dbservice);
		
		//2.0处理要保存的数据
		flushModifiedEntity(dbservice);
		
		//更新操作时间
		updateTime = System.currentTimeMillis();
	}

	private void flushModifiedEntity(HibernateDBService dbservice) {
		final List<BaseEntity> saveOrUpdateList = new ArrayList<BaseEntity>();
		IRedis redis = dbm.getiRedis();
		IPipelineOp pip = redis.pipeline();
		final List<DirtyDataInfo> dirtyList = new ArrayList<DirtyDataInfo>();
		IRedisResponse<List<IRedisResponse<?>>> results = pip.exec(new PipelineProcess() {
			
			@Override
			public void apply() {
				for (Entry<IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>>, DbOp> entry : map.entrySet()) {
					IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>> key = entry.getKey();
					DbOp oType = entry.getValue();
					try {
						key.getEntityRedisOp().getEntityFromRedis(this, key);
					} catch (Exception e) {
						logger.error("DataCache:flushModifiedEntity() key.getEntityRedisOp().getEntityFromRedis(this, key) throw Exception!", e);
						continue;
					}
					DirtyDataInfo dirtyDataInfo = new DirtyDataInfo();
					dirtyDataInfo.setRedisKey(key);
					dirtyDataInfo.setOperateType(oType);
					dirtyList.add(dirtyDataInfo);
				}
			}
		});
		
		List<IRedisResponse<?>> resultList = results.get();
		for (int i = 0; i < resultList.size(); i++) {
			@SuppressWarnings("unchecked")
			IRedisResponse<BaseEntity> iRedisResponse = (IRedisResponse<BaseEntity>) resultList.get(i);
			BaseEntity tempEntity = iRedisResponse.get();
			if (tempEntity==null) {
				//没取到entity。可能出了bug，或被恶意删除了
				DirtyDataInfo dirtyDataInfo = dirtyList.get(i);
				//如果上面的pipeline出了异常，这里报的类名可能不准
				logger.warn("DataCache:flushModifiedEntity() entity[{}] to save or update is null!", dirtyDataInfo.getRedisKey().getEntityType().getName());
				continue;
			}
			
			saveOrUpdateList.add(tempEntity);
		}

		try {
			dbservice.saveOrUpdateBatch(saveOrUpdateList);
		} catch (DataAccessException e) {
			logger.error("DataCache:flushModifiedEntity() dbservice.saveOrUpdateBatch(saveOrUpdateList) throw Exception!", e);
			logger.error("Now save one by one begin:");
			for (BaseEntity entity : saveOrUpdateList) {
				try {
					dbservice.saveOrUpdate(entity);
				} catch (DataAccessException e1) {
					logger.error("DataCache:flushModifiedEntity() dbservice.saveOrUpdate(entity) throw Exception!", e1);
				}
			}
			logger.error("Now save one by one end:");
		}
		
		map.clear();
	}

	private void flushDeleteEntity(HibernateDBService dbservice) {
		final List<EntityToDelete> delList = new ArrayList<EntityToDelete>();
		for (Iterator<Entry<IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>>, DbOp>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>>, DbOp> entry = iterator.next();
			if (entry.getValue()==DbOp.DELETE) {
				EntityToDelete entityToDelete = new EntityToDelete();
				IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>> key = entry.getKey();
				entityToDelete.entityClass = key.getEntityType();
				entityToDelete.id = key.getDbId();
				iterator.remove();
				delList.add(entityToDelete);
				continue;
			}
		}
		dbservice.deleteByIdBatch(delList);
	}

}
