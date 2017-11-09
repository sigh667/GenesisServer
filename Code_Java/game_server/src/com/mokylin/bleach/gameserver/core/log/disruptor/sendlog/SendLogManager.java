package com.mokylin.bleach.gameserver.core.log.disruptor.sendlog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.mokylin.bleach.gameserver.core.log.disruptor.LogEvent;
import com.mokylin.bleach.gameserver.core.log.disruptor.LogEventFactory;
import com.mokylin.bleach.gameserver.core.log.disruptor.LogEventHandler;
import com.mokylin.bleach.gameserver.core.log.disruptor.LogEventProducer;

/**
 * 发送日志管理器
 * @author yaguang.xiao
 *
 */
public class SendLogManager implements ISendLog {

	/** 环形buffer的大小 */
	private static final int BUFFER_SIZE = 256 * 1024;
	/** 定时向disruptor发送无效的日志事件，用来保证按照时间批量向scribe发送日志的精确度 */
	private final ScheduledExecutorService scheduler = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					Thread th = new Thread(r);
					th.setName("send log check item");
					return th;
				}

			});

	/** 用来执行disruptor的处理器（单线程） */
	private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			Thread th = new Thread(r);
			th.setName("send log");
			return th;
		}

	});
	private final Disruptor<LogEvent> disruptor;
	private final LogEventProducer producer;

	@SuppressWarnings("unchecked")
	public SendLogManager(String targetHost, int targetPort, long timeGap,
			long maxNum, long timeAccuracy) {
		LogEventFactory factory = new LogEventFactory();

		disruptor = new Disruptor<>(factory, BUFFER_SIZE, executor);

		disruptor.handleEventsWith(new LogEventHandler(targetHost, targetPort,
				timeGap, maxNum));

		disruptor.start();

		RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();

		producer = new LogEventProducer(ringBuffer);

		this.scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				// 这里为了保证时间检测的精度，这里发送的信息不会记录到日志文件去
				producer.sendLog(null);
			}
			
		}, timeAccuracy, timeAccuracy, TimeUnit.MILLISECONDS);
	}

	/**
	 * 发送日志
	 * @param category
	 * @param logStr
	 */
	public void sendLog(String logStr) {
		this.producer.sendLog(logStr);
	}

	/**
	 * 关闭整个日志系统
	 */
	public void shutdown() {
		this.disruptor.shutdown();
		this.executor.shutdown();
		this.scheduler.shutdown();
	}
}
