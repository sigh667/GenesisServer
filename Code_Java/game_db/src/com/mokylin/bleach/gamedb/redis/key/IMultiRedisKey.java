package com.mokylin.bleach.gamedb.redis.key;

import java.io.Serializable;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;

public interface IMultiRedisKey<IdType extends Serializable, T extends EntityWithRedisKey<?>> extends IRedisKey<IdType,T>{

	String getField();
}
