package com.mokylin.bleach.gamedb.uuid;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.mokylin.bleach.core.orm.BaseEntity;
import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.core.uuid.UUIDHelper;
import com.mokylin.bleach.core.uuid.helper.Scope;
import com.mokylin.bleach.core.uuid.type.IUUIDType;
import com.mokylin.bleach.gamedb.orm.entity.FunctionEntity;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gamedb.orm.entity.ItemEntity;
import com.mokylin.bleach.gamedb.orm.entity.ShopDiscountEntity;
import com.mokylin.bleach.gamedb.orm.entity.ShopEntity;
import com.mokylin.bleach.gamedb.redis.key.RedisKeyWithServerId;

/**
 * 所有需要UUID的类，在这里定义
 * @author baoliang.shen
 *
 */
public enum UUIDType implements IUUIDType {
	/**角色表*/
	Human(HumanEntity.class),
	/**道具表*/
	Item(ItemEntity.class),
	/** 商店表 */
	Shop(ShopEntity.class),
	/** 商店打折 */
	ShopDiscount(ShopDiscountEntity.class),
	/** 功能 */
	Function(FunctionEntity.class),
	;

	/**对应的数据库实体类*/
	private Class<? extends BaseEntity> entityClass;
	
	/**用于组装Redis中的key的分隔符*/
	private static final String separator = RedisKeyWithServerId.separator;
	/**UUID关键字，用于组装key*/
	private static String uuidKeyWord = "UUID";
	
	
	UUIDType(Class<? extends BaseEntity> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * 组装key
	 * @param serverId
	 * @return
	 */
	private String buildKey(int serverId) {
		StringBuilder sb = new StringBuilder();
		sb.append(serverId);
		sb.append(separator);
		sb.append(uuidKeyWord);
		sb.append(separator);
		sb.append(this.toString());
		
		return sb.toString();
	}

	@Override
	public long getOldMaxUuidFromRedis(IRedis iRedis, int serverId) {
		String key = buildKey(serverId);
		IRedisResponse<Set<Long>> iResponse = iRedis.getSetOp().smembers(key, Long.class);
		if (iResponse==null)
			return 0;
		
		Set<Long> setTmp = iResponse.get();
		if (setTmp==null || setTmp.isEmpty())
			return 0;

		TreeSet<Long> treeSet = new TreeSet<>(setTmp);
		Long last = treeSet.last();
		
		return last;
	}

	@Override
	public void putOldMaxUuidIntoRedis(IRedis iRedis, int serverId, Long oldMaxId) {
		String key = buildKey(serverId);
		iRedis.getSetOp().sadd(key, oldMaxId);
	}

	@Override
	public long qurryOldMaxUuidFromDB(HibernateDBService dbService, int serverGroup, int serverId) {
		Scope scope = UUIDHelper.getScope(serverGroup, serverId);
		final String _className = entityClass.getSimpleName();
		final String _sql = String.format("select max(id) from %s where id >=:minId and id <= :maxId", _className);
		List<? extends BaseEntity> entityList = dbService.findBySqlQueryAndParamAllT(entityClass, _sql,
				new String[] { "minId","maxId" }, new Object[] {scope.minValue, scope.maxValue});
		if (entityList==null || entityList.isEmpty())
			return 0;
		
		Object maxEntity = entityList.get(0);
		if (maxEntity==null)
			return 0;

		Long maxId = (Long)maxEntity;

		return maxId;
	}


}
