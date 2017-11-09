package com.mokylin.bleach.gamedb.redis.key;

import java.io.Serializable;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;

public abstract class AbstractRedisKey<IdType extends Serializable, T extends EntityWithRedisKey<?>> implements IRedisKey<IdType,T>{

	/**主键*/
	private IdType id;
	
	
	public AbstractRedisKey(IdType id) {
		this.id = id;
	}
	
	public abstract Integer getServerId();

	@Override
	public IdType getDbId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(getServerId());
		sb.append(separator);
		sb.append(id);
		sb.append(separator);
		sb.append(this.getClass().getName());
		return sb.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		//如果是同一个对象，必然是同一个key
		if (this == obj)
			return true;
		
		if (obj==null)
			return false;

		//不是同一种class，自然不是同一个key
		if (this.getClass()!=obj.getClass())
			return false;
		
		RedisKeyWithServerId<?,?> o = (RedisKeyWithServerId<?,?>)obj;
		Integer serverId = getServerId();
		if (!(serverId==o.getServerId() || (serverId!=null && serverId.equals(o.getServerId()))))
			return false;
		
		if (!(id==o.getDbId() || (id!=null && id.equals(o.getDbId()))))
			return false;

		return true;
	}
}
