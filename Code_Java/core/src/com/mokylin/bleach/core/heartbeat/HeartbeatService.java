package com.mokylin.bleach.core.heartbeat;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.mokylin.bleach.core.annotation.GuardedBy;
import com.mokylin.bleach.core.annotation.ThreadSafe;
import com.mokylin.bleach.core.concurrent.process.ProcessUnit;

/**
 * 心跳服务
 * 
 * <p>该服务每隔{@link #HEARTBEAT_GAP}毫秒执行一次心跳任务{@link IHeartbeat}
 * <p>心跳者{@link IHeartbeat}的注册{@link #registerHeartbeat(IHeartbeat)}和删除{@link #removeHeartbeat(IHeartbeat)}由该心跳服务的使用者自行调用
 * 
 * <h3>该服务需要启动，可通过{@link #start(ProcessUnit, long)}了解更多<br>
 * 本类是线程安全的，可以在多个线程里面访问</h3>
 * 
 * @author yaguang.xiao
 * 
 */

@ThreadSafe
public enum HeartbeatService {
	INSTANCE;
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatService.class);
	
	/** 逻辑处理单元 */
	private ProcessUnit processUnit;
	/** 心跳间的时间间隔（毫秒） */
	private long HEARTBEAT_GAP = 100;

	/** 心跳任务<心跳者, 上次调度该任务返回的Future> */
	@GuardedBy("this")
	private Map<IHeartbeat, Future<?>> heartbeats = Maps.newHashMap();
	/** 心跳调度器（单线程） */
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread thread = new Thread(r);
					thread.setName("心跳定时调度线程");
					return thread;
				}
			});

	/**
	 * 启动心跳服务
	 * 
	 * <p>心跳服务最核心的东西就是调度线程池里面的一个周期任务 <h3>see {@link #scheduleHeartbeatTask()}</h3>
	 * 因此必须先在调度线程池里面调度此任务才能使心跳服务正常工作
	 * @param processUnit 逻辑处理单元
	 * @param heartBeatGap 心跳间隔时间（单位：毫秒）
	 */
	public void start(ProcessUnit processUnit, long heartBeatGap) {
		this.processUnit = processUnit;
		this.HEARTBEAT_GAP = heartBeatGap;
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					scheduleHeartbeatTask();
				} catch (Exception e) {
					logger.error("schedule heartbeat task fail!", e);
				}
			}

		}, 0, HEARTBEAT_GAP, TimeUnit.MILLISECONDS);
	}

	/**
	 * 调度心跳任务
	 */
	private synchronized void scheduleHeartbeatTask() {
		for (Map.Entry<IHeartbeat, Future<?>> entry : this.heartbeats.entrySet()) {
			final IHeartbeat iHeartbeat = entry.getKey();
			Future<?> taskFuture = entry.getValue();

			// 之前调度的该任务还没执行完，则本次不调度该任务（否则可能会出现OutOfMemoryException）
			if (taskFuture != null && !taskFuture.isDone()) {
				continue;
			}

			this.heartbeats.put(iHeartbeat, processUnit.submitTask(
					new Runnable() {
						@Override
						public void run() {
							try {
								iHeartbeat.heartbeat();
							} catch (Exception e) {
								logger.error("heartbeat task execute fail!", e);
							}
						}

					}));
		}
	}

	/**
	 * 注册心跳者
	 * @param iHeartbeat	心跳者
	 */
	public synchronized void registerHeartbeat(IHeartbeat iHeartbeat) {
		this.heartbeats.put(iHeartbeat, null);
	}

	/**
	 * 删除心跳者
	 * 
	 * <p>使用注册时的心跳者来删除
	 * @param iHeartbeat	心跳者
	 */
	public synchronized void removeHeartbeat(IHeartbeat iHeartbeat) {
		this.heartbeats.remove(iHeartbeat);
	}
}
