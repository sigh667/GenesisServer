package com.mokylin.bleach.gamedb.redis.key;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;

import java.io.Serializable;

public abstract class RedisKeyWithServerId<IdType extends Serializable, T extends EntityWithRedisKey<?>>
        extends AbstractRedisKey<IdType, T> {

    /**当前服务器ID*/
    private final Integer serverId;


    public RedisKeyWithServerId(Integer serverId, IdType id) {
        super(id);
        this.serverId = serverId;
    }

    public Integer getServerId() {
        return serverId;
    }
}
