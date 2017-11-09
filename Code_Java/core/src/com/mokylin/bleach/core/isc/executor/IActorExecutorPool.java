package com.mokylin.bleach.core.isc.executor;

import java.util.concurrent.Executor;

import com.mokylin.bleach.core.isc.ServerType;

/**
 * 通信模块所使用的线程模型接口。
 * 
 * @author pangchong
 *
 */
public interface IActorExecutorPool {
	
	Executor select(ServerType sType, int sId);
}
