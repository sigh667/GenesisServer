package com.mokylin.bleach.remotelogserver.disruptor;

import org.apache.logging.log4j.Logger;

import com.lmax.disruptor.EventHandler;

/**
 * 日志事件处理器
 * @author yaguang.xiao
 *
 */
public class LogEventHandler implements EventHandler<LogEvent> {
	
	private final LogFactory logFactory;
	
	public LogEventHandler(LogFactory logFactory) {
		this.logFactory = logFactory;
	}
	
	@Override
	public void onEvent(LogEvent event, long sequence, boolean endOfBatch)
			throws Exception {
		Logger logger = this.logFactory.getLogger(event.getChannelId(), event.getServerId());
		if(logger == null) {
			event.clear();
			return;
		}
		
		logger.info(event.getLogStr());
		event.clear();
	}

}
