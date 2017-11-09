package com.mokylin.bleach.gamedb.persistance;

import java.io.Serializable;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;

/**
 * 需要持久化的类的接口
 * @author baoliang.shen
 *
 * @param <IdType>
 * @param <T>
 */
public interface IObjectInSql<IdType extends Serializable, T extends EntityWithRedisKey<?>> {

	/**
	 * 取得DB Id的值
	 * 
	 * @return
	 */
	IdType getDbId();
	
	/**
	 * 将业务对象转为实体对象
	 * 
	 * @return
	 */
	T toEntity();

	/**
	 * 从实体对象转换到业务对象
	 */
	void fromEntity(T entity);
	
	/**
	 * 设置当前对象为已修改状态
	 */
	void setModified();

	/**
	 * 被删除时调用
	 */
	void onDelete();
}
