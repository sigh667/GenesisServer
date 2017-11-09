package com.mokylin.bleach.gameserver.core.log.disruptor.sendlog;

/**
 * 发送日志
 * @author yaguang.xiao
 *
 */
public interface ISendLog {

	/**
	 * 发送日志
	 * @param category
	 * @param logStr
	 */
	public void sendLog(String logStr);
	
	/**
	 * 关闭整个日志系统
	 */
	public void shutdown();
}
