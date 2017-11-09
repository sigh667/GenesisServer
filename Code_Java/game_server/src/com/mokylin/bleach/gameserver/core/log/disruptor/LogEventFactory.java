package com.mokylin.bleach.gameserver.core.log.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 日志对象工厂
 * @author yaguang.xiao
 *
 */
public class LogEventFactory implements EventFactory<LogEvent> {

	@Override
	public LogEvent newInstance() {
		return new LogEvent();
	}

}
