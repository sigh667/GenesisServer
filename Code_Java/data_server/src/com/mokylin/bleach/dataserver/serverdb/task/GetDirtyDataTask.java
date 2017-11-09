package com.mokylin.bleach.dataserver.serverdb.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.core.redis.IRedisResponse;
import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;
import com.mokylin.bleach.gamedb.redis.DirtyDataInfo;
import com.mokylin.bleach.gamedb.redis.key.SpecialRedisKeyBuilder;

/**
 * 负责阻塞的等Redis中指定服中的脏数据信息
 * @author baoliang.shen
 *
 */
public class GetDirtyDataTask implements Runnable {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(GetDirtyDataTask.class);
	private ServerDBManager dbm;

	public GetDirtyDataTask(ServerDBManager serverDBManager) {
		this.dbm = serverDBManager;
	}

	@Override
	public void run() {
		//这个key在Redis中还不存在的时候，不需要手动创建
		//1.0根据serverId和指定的规则，拼接出key
		String dirtyDataKey = SpecialRedisKeyBuilder.buildDirtyDataKey(dbm.getOriginalServerId());
		
		//2.0用得出的key，阻塞的从Redis中取得一个脏数据对象
		IRedis redis = dbm.getiRedis();
		IRedisResponse<DirtyDataInfo> iResponse = redis.getListOp().blpop(dirtyDataKey, DirtyDataInfo.class);
		if (iResponse.isSuccess()) {
			PutDirtyDataToCacheTask task = new PutDirtyDataToCacheTask(dbm,iResponse.get());
			dbm.getWriteThreadExecutor().submitTask(task);
			
			//3.0进行下一次阻塞的等
			dbm.getMainThreadExecutor().submitTask(this);
		}else{
			//这个东西是异常序列化之后的字符串
			logger.error(iResponse.errorMsg());
			if (!dbm.getMainThreadExecutor().getExecutor().isShutdown()) {
				//3.0进行下一次阻塞的等
				dbm.getMainThreadExecutor().submitTask(this);
			}
			
			//TODO 处理redis关闭的情况
		}
		
	}

}
