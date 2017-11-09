package com.mokylin.bleach.remotelogserver.disruptor;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 日志管理器
 * @author yaguang.xiao
 *
 */
public class LogManager {

	/** 粉碎机数组 */
	private final LogDisruptor[] disruptors;
	/** <channelId_serverId, 粉碎机Id（用来对应粉碎机数组中的对象）> */
	private Map<String, Integer> channelServerIdToDisruptorId = Maps.newHashMap();
	/** 计数器（当前日志被写道这里的所有逻辑服务器的数量） */
	private int count;
	
	public LogManager(int threadNum, String logPath, String gameName, int logLifeTime) {
		this.disruptors = new LogDisruptor[threadNum];
		
		for(int i = 0;i < threadNum;i ++) {
			this.disruptors[i] = new LogDisruptor(logPath, gameName, logLifeTime);
		}
	}
	
	/**
	 * 获取日志Disruptor
	 * @param channelServerId
	 * @return
	 */
	public LogDisruptor getLogDisruptor(String channelServerId) {
		Integer disruptorId = this.channelServerIdToDisruptorId.get(channelServerId);
		if(disruptorId == null) {
			disruptorId = ++count;
			this.channelServerIdToDisruptorId.put(channelServerId, disruptorId);
		}
		
		return this.disruptors[disruptorId % this.disruptors.length];
	}
	
	/**
	 * 关闭日志管理器
	 */
	public void shutdown() {
		for(LogDisruptor logDisruptor : this.disruptors) {
			logDisruptor.shutdown();
		}
	}
	
}
