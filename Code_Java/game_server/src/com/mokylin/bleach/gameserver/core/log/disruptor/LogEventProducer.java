package com.mokylin.bleach.gameserver.core.log.disruptor;

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
	 * 产生日志
	 * @param logStr
	 */
	public void sendLog(String logStr) {
		long sequence = this.ringBuffer.next();
		
		try{
			LogEvent event = this.ringBuffer.get(sequence);
			
			event.setLogStr(logStr);
		} finally {
			this.ringBuffer.publish(sequence);
		}
	}
	
}
