package com.genesis.gamedb.redis;

import com.genesis.gamedb.orm.EntityWithRedisKey;
import com.genesis.gamedb.redis.key.IRedisKey;

import java.io.Serializable;

/**
 * 描述一个脏数据信息
 * @author Joey
 *
 */
public class DirtyDataInfo {

    /**脏数据的操作类型*/
    private DbOp dbOp;
    /**描述脏数据的key，通过调用此key的方法，就能找到对应的Entity*/
    private IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>> redisKey;

    public DbOp getDbOp() {
        return dbOp;
    }

    public void setOperateType(DbOp dbOp) {
        this.dbOp = dbOp;
    }

    public IRedisKey<? extends Serializable, ? extends EntityWithRedisKey<?>> getRedisKey() {
        return redisKey;
    }

    public <V extends Serializable, T extends EntityWithRedisKey<?>> void setRedisKey(
            IRedisKey<V, T> redisKey) {
        this.redisKey = redisKey;
    }
}
