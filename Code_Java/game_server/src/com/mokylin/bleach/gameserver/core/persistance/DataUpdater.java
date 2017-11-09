package com.mokylin.bleach.gameserver.core.persistance;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.persistance.PersistanceWrapper;
import com.mokylin.bleach.gamedb.redis.DbOp;
import com.mokylin.bleach.gamedb.redis.DirtyDataInfo;
import com.mokylin.bleach.gamedb.redis.key.IRedisKey;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.persistance.model.DirtyData;
import com.mokylin.bleach.gameserver.core.persistance.task.PersistanceIntoRedisTask;

/**
 * 负责缓冲逻辑层的脏数据，然后发送到操作Redis的线程保存
 * @author baoliang.shen
 *
 */
public class DataUpdater implements IDataUpdater{

	/** 日志 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** 该DataUpdater的宿主的名字，用于记录日志。 */
	private final String hostName;
	
	private final ServerGlobals sGlobals;
	
	//满足下列条件之一，就应该将缓存数据刷到数据库了
	/**数据量*/
	private final int maxSizeToUpdate;
	/**时间*/
	private final long maxTimeMillisToUpdate;

	/**每次flush应该在这个时间内完成，超出的话记录日志*/
	private final long TIME_VALVE = TimeUnit.MILLISECONDS.toNanos(30);

	/**记录最老的那个缓存数据进入的时间点*/
	private long updateTime;

	/**
	 * 存储被修改过且尚未同步到数据库的对象的PersistanceWrapper及需要做的操作
	 */
	private HashMap<PersistanceWrapper, WrapperData> map = new HashMap<PersistanceWrapper, WrapperData>();
	
	public DataUpdater(String hostName, int maxSizeToUpdate, long millisSecond, ServerGlobals sGlobals){
		checkArgument(hostName!=null && !hostName.isEmpty());
		this.hostName = hostName;
		this.maxSizeToUpdate = maxSizeToUpdate;
		this.maxTimeMillisToUpdate = millisSecond;
		this.sGlobals = checkNotNull(sGlobals);
	}

	/**
	 * 如果达到更新阀值了，就执行更新操作。阀值如下，达到任何一个即为满足
	 * <p>1.数量达到上限；
	 * <p>2.持续一定时间没有保存过了；
	 */
	public void heartbeat() {
		if (isNeedFlush()) {

			final long count = this.map.size();
			long start = System.nanoTime();
			flush();
			long flushDuaration = System.nanoTime() - start;

			if (flushDuaration > TIME_VALVE)
				logger.warn(hostName + " .update 数量" + count + " 时间过长：" + TimeUnit.NANOSECONDS.toMillis(flushDuaration) + "ms");
			else if (flushDuaration > 0 && logger.isDebugEnabled())
				logger.debug("[#GS.CommonDataUpdater.flush] [Update Time:" + TimeUnit.NANOSECONDS.toMillis(flushDuaration) + "ms]");
		}
	}

	/**
	 * 是否需要立刻将需要保存的数据刷到redis
	 * @return
	 */
	public boolean isNeedFlush() {

		if (map.isEmpty())
			return false;
		if (map.size() >= maxSizeToUpdate)
			return true;

		long now = Globals.getTimeService().now();
		if (now-updateTime >= maxTimeMillisToUpdate)
			return true;

		return false;
	}

	/**
	 * 将需要保存的数据刷到redis
	 */
	public void flush() {
		final List<DirtyData> dirtyList = new ArrayList<DirtyData>();
		for (Iterator<Entry<PersistanceWrapper, WrapperData>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<PersistanceWrapper, WrapperData> entry = iterator.next();
			DirtyData dirtyData = new DirtyData();
			
			try {
				EntityWithRedisKey<?> entity = entry.getKey().getSqlObject().toEntity();
				dirtyData.setEntity(entity);
			} catch (Exception e) {
				logger.error("DataUpdater:flush() entry.getKey().getSqlObject().toEntity() throw Exception!", e);
				iterator.remove();
				continue;
			}

			DirtyDataInfo dirtyDataInfo = new DirtyDataInfo();
			dirtyDataInfo.setOperateType(entry.getValue().op);
			
			IRedisKey<? extends Serializable, ?> redisKey = null;
			try {
				redisKey = (IRedisKey<? extends Serializable, ?>)dirtyData.getEntity().newRedisKey(sGlobals.getServerId());
			} catch (Exception e) {
				logger.error("DataUpdater:flush() dirtyData.entity.newRedisKey() throw Exception!", e);
				iterator.remove();
				continue;
			}
			dirtyDataInfo.setRedisKey(redisKey);
			dirtyData.setDirtyDataInfo(dirtyDataInfo);

			dirtyList.add(dirtyData);
		}
		
		PersistanceIntoRedisTask task = new PersistanceIntoRedisTask(sGlobals.getServerId(), dirtyList, sGlobals.getRedis());
		sGlobals.getRedisProcessUnit().submitTask(task);
		
		map.clear();
		updateTime = Globals.getTimeService().now();
	}

	/**
	 * 添加需要更新的对象
	 * @param wrapper
	 */
	public void addUpdate(PersistanceWrapper wrapper) {
		add(wrapper,DbOp.UPDATE);
	}

	/**
	 * 添加需要删除的对象
	 * @param wrapper
	 */
	public void addDelete(PersistanceWrapper wrapper) {
		add(wrapper,DbOp.DELETE);
	}

	private void add(PersistanceWrapper wrapper, DbOp op) {
		if (wrapper==null) {
			logger.warn("CommonDataUpdater:add(PersistanceWrapper wrapper, DbOp op) wrapper is null!");
			return;
		}

		if (map.isEmpty()) {
			updateTime = Globals.getTimeService().now();
		}

		if (!map.containsKey(wrapper)) {
			WrapperData wrapperData = new WrapperData(wrapper, op);
			map.put(wrapper, wrapperData);
		} else {
			WrapperData oldWrapperData = map.get(wrapper);
			PersistanceWrapper oldWrapper = oldWrapperData.wrapper;
			/**
			 * 指向的是数据库中的同一条数据，但却是内存中的不同对象<br>
			 * 出现这种情况，一般是对象被modify了，还没来得及保存，对象被销毁，又从某渠道new了这个对象，又modify了<br>
			 * 比如：英雄离队，此时删除对象，很快英雄又归队<br>
			 * 出现这种情况，就证明出现了上述这种不好的写法（应该改为：英雄离队，也应该继续持有着这个对象，只是状态置为“离队”，直到玩家下线）<br>
			 */
			if (oldWrapper!=wrapper) {
				logger.warn("持久化时，包装类不同但却指向同一ID的数据Class=【{}】ID=【{}】",
						oldWrapper.getSqlObject().getClass().getName(), oldWrapper.getSqlObject().getDbId());
				flush();
				
				WrapperData wrapperData = new WrapperData(wrapper, op);
				map.put(wrapper, wrapperData);
			} else {
				final DbOp oldDbOp = oldWrapperData.op;
				oldWrapperData.op = oldDbOp.merge(op);
			}
		}

		if (isNeedFlush()) {
			flush();
		}
	}
	
	public static class WrapperData {
		PersistanceWrapper wrapper;
		DbOp op;
		
		public WrapperData(PersistanceWrapper wrapper, DbOp op) {
			this.wrapper = wrapper;
			this.op = op;
		}
	}
}
