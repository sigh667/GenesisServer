package com.mokylin.bleach.remotelogserver.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 日志事件工厂类
 * @author yaguang.xiao
 *
 */
public class LogEventFactory implements EventFactory<LogEvent> {

	@Override
	public LogEvent newInstance() {
		return new LogEvent();
	}

}