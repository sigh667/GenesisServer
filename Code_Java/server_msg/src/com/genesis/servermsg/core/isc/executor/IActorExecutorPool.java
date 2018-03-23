package com.genesis.servermsg.core.isc.executor;

import com.genesis.servermsg.core.isc.ServerType;

import java.util.concurrent.Executor;

/**
 * 通信模块所使用的线程模型接口。
 *
 * @author pangchong
 *
 */
public interface IActorExecutorPool {

    Executor select(ServerType sType, int sId);
}
