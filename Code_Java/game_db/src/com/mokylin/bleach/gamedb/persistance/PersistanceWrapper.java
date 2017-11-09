package com.mokylin.bleach.gamedb.persistance;

import java.io.Serializable;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;

/**
 * 用于放进全局脏数据列表的包装类
 * @author baoliang.shen
 *
 */
public class PersistanceWrapper {

	final private IObjectInSql<? extends Serializable, ? extends EntityWithRedisKey<?>> sqlObject;
	
	
	public PersistanceWrapper(IObjectInSql<? extends Serializable, ? extends EntityWithRedisKey<?>> sqlObject) {
		this.sqlObject = sqlObject;
	}
	
	@Override
	public int hashCode() {
		return sqlObject.getDbId().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		//如果是同一个对象，必然是同一个key
		if (this == obj)
			return true;
		
		if (obj==null)
			return false;

		//不是同一种对象，自然不是同一个key
		if (!this.getClass().equals(obj.getClass()))
			return false;
		
		PersistanceWrapper other = (PersistanceWrapper)obj;
		
		IObjectInSql<? extends Serializable, ? extends EntityWithRedisKey<?>> otherSO = other.getSqlObject();
		if (this.sqlObject==otherSO)
			return true;
		if (otherSO==null)
			return false;
		
		if (!otherSO.getClass().equals(sqlObject.getClass()))
			return false;
		
		Serializable otherId = otherSO.getDbId();
		if (otherId==sqlObject.getDbId() || (otherId!=null && otherId.equals(sqlObject.getDbId()))) {
			return true;
		} else {
			return false;
		}
	}

	public IObjectInSql<? extends Serializable, ? extends EntityWithRedisKey<?>> getSqlObject() {
		return sqlObject;
	}
}
