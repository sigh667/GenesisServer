package com.mokylin.bleach.core.isc.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.isc.ServerType;

/**
 * ISC通信模块中线程模型的默认实现。<p>
 * 
 * 在每次获取一个工作线程时，该线程模型会根据服务器的类型和id选择一个工作线程，但是该实现保证对于同一个
 * 服务器类型和id，每次获得的都是相同的工作线程，从而保证对于同一个服务器发送来的消息的处理是串行的。<p>
 * 
 * 在创建该实现的实例时，如果没有显式的指定使用的线程数量，则默认为单线程。
 * 
 * @author pangchong
 *
 */
public class BindServerActorExecutorPool implements IActorExecutorPool {
	
	private final static int DEFAULT_THREAD_NUM = 1;
	
	private Executor[] executors = null;
	
	private Table<ServerType, Integer, Integer> spuIdTable = HashBasedTable.create();
	
	private int count = 0;
	
	public BindServerActorExecutorPool() {
		this(DEFAULT_THREAD_NUM);
	}
	
	public BindServerActorExecutorPool(int threadNum){
		executors = new Executor[threadNum];
		for(int i=0; i<executors.length; i++){
			executors[i] = Executors.newSingleThreadExecutor();
		}
	}

	@Override
	public Executor select(ServerType sType, int sId) {
		if(executors.length == DEFAULT_THREAD_NUM) return executors[0];
		
		Integer spuId = spuIdTable.get(sType, sId);
		if(spuId == null){
			spuId = (count = count + 1);
			spuIdTable.put(sType, sId, spuId);
		}
		return executors[spuId % (executors.length)];
	}

}
