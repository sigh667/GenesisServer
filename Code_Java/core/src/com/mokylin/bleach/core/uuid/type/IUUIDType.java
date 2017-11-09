package com.mokylin.bleach.core.uuid.type;

import com.mokylin.bleach.core.orm.hibernate.HibernateDBService;
import com.mokylin.bleach.core.redis.IRedis;

/**
 * 用来定义UUID类型
 * 
 * <p>所有实现此接口的类都是一个UUID的类型<br>
 * 要支持一个新类型的UUID，只需要在com.mokylin.bleach.core.uuid.type包里面添加一个本接口的实现
 * 
 * @author yaguang.xiao
 *
 */

public interface IUUIDType {
	
	/**
	 * 从Redis中加载某服中本类型的UUID的最大值
	 * @param iRedis
	 * @param serverId 服务器ID
	 * @return
	 */
	long getOldMaxUuidFromRedis(IRedis iRedis, int serverId);

	/**
	 * 将 某服中本类型的UUID的最大值 存入Redis
	 * @param iRedis
	 * @param serverId 服务器ID
	 * @param oldMaxId
	 */
	public void putOldMaxUuidIntoRedis(IRedis iRedis, int serverId, Long oldMaxId);

	/**
	 * 从数据库中加载某服中本类型的UUID的最大值
	 * @param dbService
	 * @param serverGroup TODO
	 * @param serverId TODO
	 * @return
	 */
	long qurryOldMaxUuidFromDB(HibernateDBService dbService, int serverGroup, int serverId);
}
