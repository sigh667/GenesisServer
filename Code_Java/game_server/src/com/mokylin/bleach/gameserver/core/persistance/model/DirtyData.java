package com.mokylin.bleach.gameserver.core.persistance.model;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.redis.DirtyDataInfo;

/**
 * 用于脏数据保存的类
 * @author baoliang.shen
 *
 */
public class DirtyData {
	private DirtyDataInfo dirtyDataInfo;
	private EntityWithRedisKey<?> entity;

	public DirtyDataInfo getDirtyDataInfo() {
		return dirtyDataInfo;
	}
	public void setDirtyDataInfo(DirtyDataInfo dirtyDataInfo) {
		this.dirtyDataInfo = dirtyDataInfo;
	}
	public EntityWithRedisKey<?> getEntity() {
		return entity;
	}
	public void setEntity(EntityWithRedisKey<?> entity) {
		this.entity = entity;
	}
}
