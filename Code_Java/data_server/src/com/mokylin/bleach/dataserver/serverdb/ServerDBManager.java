package com.mokylin.bleach.dataserver.serverdb;

import java.util.concurrent.Future;

import com.mokylin.bleach.core.concurrent.process.ProcessUnit;
import com.mokylin.bleach.core.concurrent.process.ProcessUnitHelper;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.dataserver.serverdb.task.GetDirtyDataTask;
import com.mokylin.bleach.dataserver.serverdb.task.LoadActiveHumanDataTask;
import com.mokylin.bleach.dataserver.serverdb.task.LoadGlobalDataTask;

/**
 * 负责单个服的数据操作
 * @author baoliang.shen
 *
 */
public class ServerDBManager {

	private int originalServerId;
	private int currentServerId;
	private IRedis iRedis;

	/**负责读MySql的线程*/
	private ProcessUnit readThreadExecutor;

	/**负责分派任务的线程*/
	private ProcessUnit mainThreadExecutor;

	/**负责写MySql的线程*/
	private ProcessUnit writeThreadExecutor;

	/**在DataServer内部负责缓冲数据的容器，凑齐1W条或等待10秒钟就写进数据库*/
	private DataCache dataCache;

	public ServerDBManager(int originalServerId, int currentServerId, IRedis iRedis) {
		this.originalServerId = originalServerId;
		this.currentServerId = currentServerId;
		this.iRedis = iRedis;
		this.dataCache = new DataCache(this);

		readThreadExecutor = ProcessUnitHelper.createSingleProcessUnit(originalServerId+"区read线程，当前隶属于"+currentServerId+"区");
		mainThreadExecutor = ProcessUnitHelper.createSingleProcessUnit(originalServerId+"区main线程，当前隶属于"+currentServerId+"区");
		writeThreadExecutor = ProcessUnitHelper.createSingleProcessUnit(originalServerId+"区write线程，当前隶属于"+currentServerId+"区");
	}


	/**
	 * 加载全局数据和账号数据
	 * @return
	 */
	public Future<?> loadGlobalData() {
		return readThreadExecutor.submitTask(new LoadGlobalDataTask(this));
	}

	/**
	 * 加载所有活跃玩家数据
	 * @return
	 */
	public Future<?> loadActiveHumanData() {
		return readThreadExecutor.submitTask(new LoadActiveHumanDataTask(this));
	}

	/**
	 * 开始取Redis中的脏数据
	 * @return
	 */
	public void beginReadDirtyData() {
		GetDirtyDataTask task = new GetDirtyDataTask(this);
		mainThreadExecutor.submitTask(task);
	}


	public int getOriginalServerId() {
		return originalServerId;
	}

	public int getCurrentServerId() {
		return currentServerId;
	}

	public ProcessUnit getReadThreadExecutor() {
		return readThreadExecutor;
	}

	public ProcessUnit getWriteThreadExecutor() {
		return writeThreadExecutor;
	}

	public ProcessUnit getMainThreadExecutor() {
		return mainThreadExecutor;
	}


	public IRedis getiRedis() {
		return iRedis;
	}

	public DataCache getDataCache() {
		return dataCache;
	}


	public void shutdown() {
		// TODO Auto-generated method stub
		readThreadExecutor.getExecutor().shutdown();
		mainThreadExecutor.getExecutor().shutdown();
		writeThreadExecutor.getExecutor().shutdown();
	}


}
