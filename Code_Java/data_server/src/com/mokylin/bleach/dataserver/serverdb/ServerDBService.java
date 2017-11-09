package com.mokylin.bleach.dataserver.serverdb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.config.Mapping;
import com.mokylin.bleach.core.config.ServerAddressTable;
import com.mokylin.bleach.core.config.model.ServerInfo;
import com.mokylin.bleach.core.heartbeat.IHeartbeat;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.redis.IRedis;
import com.mokylin.bleach.dataserver.conf.DataServerConfig;
import com.mokylin.bleach.dataserver.globals.Globals;
import com.mokylin.bleach.dataserver.redis.RedisManager;
import com.mokylin.bleach.dataserver.serverdb.task.CheckDataCacheTask;
import com.mokylin.bleach.gamedb.uuid.UUIDType;

/**
 * 管理所有服的数据操作
 * @author baoliang.shen
 *
 */
public class ServerDBService implements IHeartbeat {
	
	/** 日志 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**map<原服务器ID，负责操作此服的管理器>*/
	private HashMap<Integer, ServerDBManager> dbMngMap = new HashMap<Integer, ServerDBManager>();
	/**监视每个ServerDBManager的心跳任务是否执行完毕*/
	private HashMap<ServerDBManager,Future<?>> heartbeats = new HashMap<ServerDBManager,Future<?>>();
	

	public void start(RedisManager redisManager) {
		// 注册心跳
		Globals.getHeartBeatService().registerHeartbeat(this);
		
		// 启动每个服的ServerDBManager
		Mapping mappingConf = Globals.getServerConfig().mappingConf;
		for (Entry<Integer, Integer> entry : mappingConf.getGs_dbs_Map().entrySet()) {
			if (entry.getValue()!=Globals.getServerConfig().serverConfig.serverId)
				continue;
			
			Integer originalServerId = entry.getKey();
			IRedis iRedis = redisManager.getIRedis(originalServerId);
			Integer currentServerId = mappingConf.getOriginalgs_currentgs_map().get(originalServerId);
			ServerDBManager serverDBManager = new ServerDBManager(originalServerId,currentServerId,iRedis);
			dbMngMap.put(originalServerId, serverDBManager);
		}
	}

	/**
	 * 加载所有全局数据和账号数据
	 */
	private LinkedList<Future<?>> loadGlobalData() {
		LinkedList<Future<?>> futures = new LinkedList<Future<?>>();
		for (ServerDBManager dbm : dbMngMap.values()) {
			Future<?> future = dbm.loadGlobalData();
			futures.add(future);
		}
		return futures;
	}

	/**
	 * 加载所有活跃玩家数据
	 */
	private LinkedList<Future<?>> loadActiveHumanData() {
		LinkedList<Future<?>> futures = new LinkedList<Future<?>>();
		for (ServerDBManager dbm : dbMngMap.values()) {
			Future<?> future = dbm.loadActiveHumanData();
			futures.add(future);
		}
		return futures;
	}

	/**
	 * 加载必要数据，阻塞的
	 * @param redisManager 
	 */
	public void loadNecessaryData(RedisManager redisManager) {
		// 用于查询所有任务是否完成
		LinkedList<Future<?>> futures = new LinkedList<>();

		// 加载所有全局数据和账号数据
		futures.addAll(loadGlobalData());

		// 加载所有活跃玩家数据
		futures.addAll(loadActiveHumanData());
		
		// 加载UUID的最大值
		// 因为量不大，况且本线程在空闲，所以此任务就在本线程执行
		loadOldMaxUUID(redisManager);
		
		// 阻塞的等所有加载完成
		while (!futures.isEmpty()) {
			Future<?> future = futures.poll();
			try {
				future.get();
			} catch (InterruptedException e1) {
				logger.error("loadNecessaryData()的future.get()异常", e1);
			} catch (ExecutionException e1) {
				logger.error("loadNecessaryData()的future.get()异常", e1);
			}
		}
	}

	/**
	 * 加载UUID的最大值
	 * @param redisManager 
	 */
	private void loadOldMaxUUID(RedisManager redisManager) {

		HashSet<ServerInfo> set = new HashSet<>();
		DataServerConfig dataServerConfig = Globals.getServerConfig();
		Mapping mappingConf = Globals.getServerConfig().mappingConf;
		ServerAddressTable addressConf = Globals.getServerAddressTable();
		Map<Integer, Integer> originalgs_currentgs_map = mappingConf.getOriginalgs_currentgs_map();
		for (Entry<Integer, Integer> entry : mappingConf.getGs_dbs_Map().entrySet()) {
			if (entry.getValue()!=dataServerConfig.serverConfig.serverId)
				continue;

			Integer currentgsId = originalgs_currentgs_map.get(entry.getKey());
			ServerInfo serverInfo = addressConf.getTable().get(ServerType.GAME_SERVER, currentgsId);
			set.add(serverInfo);
		}

		for (ServerInfo serverInfo : set) {
			final int serverGroup = serverInfo.getServerGroup();
			final int serverId = serverInfo.getServerID();
			for (UUIDType uuidType : UUIDType.values()) {
				final long oldMaxId = uuidType.qurryOldMaxUuidFromDB(Globals.getDbservice(),
						serverGroup, serverId);
				
				IRedis iRedis = redisManager.getIRedis(serverId);
				uuidType.putOldMaxUuidIntoRedis(iRedis, serverId, oldMaxId);
			}
		}
	}

	/**
	 * 发送消息，开始取Redis中的脏数据
	 */
	public void beginReadDirtyData() {
		for (ServerDBManager dbm : dbMngMap.values()) {
			dbm.beginReadDirtyData();
		}
	}

	@Override
	public void heartbeat() {
		for (ServerDBManager dbm : dbMngMap.values()) {
			Future<?> taskFuture = heartbeats.get(dbm);
			// 之前调度的该任务还没执行完，则本次不调度该任务（否则可能会出现OutOfMemoryException）
			if (taskFuture != null && !taskFuture.isDone()) {
				continue;
			}
			
			CheckDataCacheTask task = new CheckDataCacheTask(dbm);
			Future<?> future = dbm.getWriteThreadExecutor().submitTask(task);
			heartbeats.put(dbm, future);
		}
	}

	/**
	 * 取ServerDBManager
	 * @param originalServerId	原服ID
	 * @return
	 */
	public ServerDBManager getServerDBManager(int originalServerId) {
		return dbMngMap.get(originalServerId);
	}

	public void shutdown() {
		for (ServerDBManager dbm : dbMngMap.values()) {
			dbm.shutdown();
		}
	}
}
