package com.genesis.gamedb.redis.key.model;

import com.genesis.gamedb.redis.key.RedisKeyWithServerId;
import com.genesis.gamedb.orm.entity.FunctionEntity;
import com.genesis.gamedb.redis.key.AbstractRedisKey;
import com.genesis.gamedb.redis.key.IMultiRedisKey;
import com.genesis.gamedb.redis.key.RedisKeyType;
import com.genesis.gamedb.redis.redisop.IEntityRedisOp;
import com.genesis.gamedb.redis.redisop.model.EntityMultiKeyRedisOp;

public class FunctionKey extends RedisKeyWithServerId<Long, FunctionEntity>
        implements IMultiRedisKey<Long, FunctionEntity> {

    /** 玩家Id */
    private final long humanId;
    /** 功能Id */
    private final int functionId;

    public FunctionKey(Integer serverId, long humanId, int functionId, Long id) {
        super(serverId, id);
        this.humanId = humanId;
        this.functionId = functionId;
    }

    @Override
    public String getKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getServerId());
        sb.append(AbstractRedisKey.separator);
        sb.append(RedisKeyType.Function.toString());
        sb.append(AbstractRedisKey.separator);
        sb.append(humanId);
        return sb.toString();
    }

    @Override
    public Class<FunctionEntity> getEntityType() {
        return FunctionEntity.class;
    }

    @Override
    public IEntityRedisOp getEntityRedisOp() {
        return EntityMultiKeyRedisOp.INSTANCE;
    }

    @Override
    public String getField() {
        return String.valueOf(this.functionId);
    }

}
