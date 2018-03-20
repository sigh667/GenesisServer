package com.genesis.core.redis.op;

import com.genesis.core.serializer.ISerializer;
import com.genesis.core.redis.IRedisResponse;

import java.util.LinkedList;
import java.util.Queue;

import redis.clients.jedis.Client;

/**
 * 批量操作类型的命令执行器。<p>
 *
 * @author pangchong
 *
 */
public class CmdProcessExecutor implements IRedisCmdExecutor {

    final Client jedis;
    final ISerializer serializer;
    private Queue<SingleRedisCmd<?>> executedCmds = new LinkedList<>();

    public CmdProcessExecutor(Client jedis, ISerializer serializer) {
        this.jedis = jedis;
        this.serializer = serializer;
    }

    public <R> IRedisResponse<R> execCommand(SingleRedisCmd<R> func) {
        func.apply(jedis, serializer);
        executedCmds.add(func);
        return null;
    }

    /**
     * 获取批量执行过的指令。
     *
     * @return
     */
    public Queue<SingleRedisCmd<?>> getExecutedCmdQueue() {
        return executedCmds;
    }
}
