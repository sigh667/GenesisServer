package com.mokylin.bleach.gameserver.core.config;

/**
 * 日志配置
 * @author yaguang.xiao
 *
 */
public class LogConfig {

	/** scribe主机地址 */
	private final String scribeHost;
	/** scribe端口 */
	private final int scribePort;
	/** 批量发送最大时间间隔 */
	private final long flushTimeGap;
	/** 最大缓存日志条数 */
	private final long cacheMaxNum;
	/** 批量发送时间精度 */
	private final long flushTimeAccuracy;
	/** 是否需要模拟掉SendLog接口，调试的时候需要模拟 */
	private final boolean mockSendLog;
	
	public LogConfig(String scribeHost, int scribePort, long flushTimeGap, long cacheMaxNum, long flushTimeAccuracy, boolean mockSendLog) {
		this.scribeHost = scribeHost;
		this.scribePort = scribePort;
		this.flushTimeGap = flushTimeGap;
		this.cacheMaxNum = cacheMaxNum;
		this.flushTimeAccuracy = flushTimeAccuracy;
		this.mockSendLog = mockSendLog;
	}

	public String getScribeHost() {
		return scribeHost;
	}

	public int getScribePort() {
		return scribePort;
	}

	public long getFlushTimeGap() {
		return flushTimeGap;
	}

	public long getCacheMaxNum() {
		return cacheMaxNum;
	}

	public long getFlushTimeAccuracy() {
		return flushTimeAccuracy;
	}

	public boolean isMockSendLog() {
		return mockSendLog;
	}
}
