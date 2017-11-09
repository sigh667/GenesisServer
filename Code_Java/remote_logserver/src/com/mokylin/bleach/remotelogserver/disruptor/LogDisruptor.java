package com.mokylin.bleach.remotelogserver.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 日志粉碎机，此类的每一个对象可以当成一个单线程处理器
 * 它采用Discruptor技术来进行线程间数据交换
 * @author yaguang.xiao
 *
 */
public class LogDisruptor {
	
	/** 环形buffer的大小 */
	private static final int BUFFER_SIZE = 256 * 1024;
	/** 用来跑LogEventHandler的线程 */
	private final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			Thread th = new Thread(r);
			th.setName("log");
			return th;
		}
		
	});
	
	private final Disruptor<LogEvent> disruptor;
	private final LogEventProducer producer;
	
	@SuppressWarnings("unchecked")
	public LogDisruptor(String logPath, String gameName, int logLifeTime) {
		LogEventFactory factory = new LogEventFactory();
		
		disruptor = new Disruptor<>(factory, BUFFER_SIZE, executor);
		
		LogFactory logFactory = new LogFactory(logPath, gameName, logLifeTime);
		disruptor.handleEventsWith(new LogEventHandler(logFactory));
		
		disruptor.start();
		
		RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();
		
		producer = new LogEventProducer(ringBuffer);
	}
	
	public void log(String channelId, String serverId, String logStr) {
		this.producer.log(channelId, serverId, logStr);
	}
	
	public void shutdown() {
		this.disruptor.shutdown();
		this.executor.shutdown();
	}
}
