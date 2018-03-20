package com.genesis.core.isc.executor;

import com.genesis.core.isc.ServerType;

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
