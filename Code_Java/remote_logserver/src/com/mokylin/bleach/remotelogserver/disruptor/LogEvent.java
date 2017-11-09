package com.mokylin.bleach.remotelogserver.disruptor;

public class LogEvent {

	private String channelId;
	private String serverId;
	/** 日志内容 */
	private String logStr;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getLogStr() {
		return logStr;
	}

	public void setLogStr(String logStr) {
		this.logStr = logStr;
	}
	
	public void clear() {
		this.channelId = null;
		this.serverId = null;
		this.logStr = null;
	}

}
