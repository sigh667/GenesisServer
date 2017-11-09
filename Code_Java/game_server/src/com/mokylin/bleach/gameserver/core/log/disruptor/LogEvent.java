package com.mokylin.bleach.gameserver.core.log.disruptor;

/**
 * 日志事件
 * @author yaguang.xiao
 *
 */
public class LogEvent {
	/** 日志内容 */
	private String logStr;
	
	public String getLogStr() {
		return this.logStr;
	}
	
	public void setLogStr(String logStr) {
		this.logStr = logStr;
	}

	public void clear() {
		this.logStr = null;
	}
}
