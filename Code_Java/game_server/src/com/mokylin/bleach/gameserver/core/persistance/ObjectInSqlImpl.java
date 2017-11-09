package com.mokylin.bleach.gameserver.core.persistance;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.persistance.IObjectInSql;
import com.mokylin.bleach.gamedb.persistance.PersistanceWrapper;

/**
 * GameServer中的需要持久化的类的基类<p>
 * 如果要持久化的对象会频繁变化，那么请继承本类<p>
 * 本类会在一定时间内缓冲需要保存的数据，然后一次性提交到Redis线程
 * <p>由于java不能多继承，如果你的类还需要继承别的类，那么请你实现IObjectInSql接口
 * @author baoliang.shen
 *
 * @param <IdType>
 * @param <T>
 */
public abstract class ObjectInSqlImpl<IdType extends Serializable, T extends EntityWithRedisKey<?>> implements IObjectInSql<IdType, T> {
	
	private final PersistanceWrapper persistanceWrapper;
	
	private final IDataUpdater dataUpdater; 
	
	public ObjectInSqlImpl(IDataUpdater dataUpdater) {
		this.dataUpdater = checkNotNull(dataUpdater);
		this.persistanceWrapper = new PersistanceWrapper(this);
	}
	public PersistanceWrapper getPersistanceWrapper() {
		return persistanceWrapper;
	}
	
	/**
	 * 设置当前对象为已修改状态
	 */
	@Override
	public void setModified() {
		dataUpdater.addUpdate(persistanceWrapper);
	}

	/**
	 * 被删除时调用
	 */
	@Override
	public void onDelete() {
		dataUpdater.addDelete(persistanceWrapper);
	}
	public IDataUpdater getDataUpdater() {
		return dataUpdater;
	}
}
