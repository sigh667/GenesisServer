package com.genesis.core.redis;

import com.genesis.core.redis.op.IPipelineOp;
import com.genesis.core.redis.op.ISetOp;
import com.genesis.core.redis.op.IHashOp;
import com.genesis.core.redis.op.IListOp;
import com.genesis.core.redis.op.ITransactionOp;
import com.genesis.core.redis.op.IValueOp;

public interface IRedis {

    IValueOp getValueOp();

    IHashOp getHashOp();

    IListOp getListOp();

    ISetOp getSetOp();

    IPipelineOp pipeline();

    ITransactionOp multi();

    String getServerKey();

    /**
     * 检测当前Redis的连接是否正常。
     *
     * @return
     */
    boolean isConnected();
}
