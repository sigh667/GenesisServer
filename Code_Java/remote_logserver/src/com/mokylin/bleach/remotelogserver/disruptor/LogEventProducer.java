package com.mokylin.bleach.remotelogserver.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * 日志事件的生产者
 * @author yaguang.xiao
 *
 */
public class LogEventProducer {

	/** 环形buffer */
	private final RingBuffer<LogEvent> ringBuffer;
	
	public LogEventProducer(RingBuffer<LogEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}
	
	/**
	 * 发布日志事件
	 * @param channelId
	 * @param serverId
	 * @param logStr
	 */
	public void log(String channelId, String serverId, String logStr) {
		if(this.isStringInvalid(channelId) || this.isStringInvalid(serverId) || this.isStringInvalid(logStr)) {
			return;
		}
		
		long sequence = this.ringBuffer.next();
		
		try{
			LogEvent event = this.ringBuffer.get(sequence);
			
			event.setChannelId(channelId);
			event.setServerId(serverId);
			event.setLogStr(logStr);
		} finally {
			this.ringBuffer.publish(sequence);
		}
	}
	
	/**
	 * 判断字符串是不是有效的
	 * @param str
	 * @return
	 */
	private boolean isStringInvalid(String str) {
		if(str == null || str.trim().isEmpty())
			return true;
		
		return false;
	}
	
}
