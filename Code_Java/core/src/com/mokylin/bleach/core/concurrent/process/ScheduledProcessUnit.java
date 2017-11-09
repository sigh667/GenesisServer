package com.mokylin.bleach.core.concurrent.process;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 用于进行计时操作的定时器。<p>
 * 
 * 和逻辑线程一样，可以向该定时器提交task的同时指定该task执行的时间。
 * 需要注意的时候，如果使用了没有指定时间的方法，则会认为提交的task需要
 * 立即执行。<p>
 * 
 * <b>注意：</b>提交的任务中可能抛出Exception，该定时器将忽略这些Exception继续执行，
 * 只会将Exception的信息输出的日志中。另外，该定时器中提交的任务在执行的时候是并行执行的。
 * 
 * @author pangchong
 *
 */
public class ScheduledProcessUnit extends ProcessUnit{
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledProcessUnit.class);

	ScheduledProcessUnit(String threadName, ScheduledExecutorService pool) {
		super(threadName, pool);
	}

	public void scheduleAtFixedRate(final Runnable task, long initDelay, long period, TimeUnit timeUnit){
		this.getScheduleExecutor().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try{
					task.run();
				}catch(Throwable t){
					log.error(errorLogContent, t);
				}finally{
					taskCount.decrementAndGet();
				}
			}
		}, initDelay, period, timeUnit);
	}
	
	public ScheduledFuture<?> schedule(final Runnable task, long delay, TimeUnit timeUnit){
		return this.getScheduleExecutor().schedule(new Runnable() {
			@Override
			public void run() {
				try{
					task.run();
				}catch(Throwable t){
					log.error(errorLogContent, t);
				}finally{
					taskCount.decrementAndGet();
				}
			}
		}, delay, timeUnit);
	}
	
	public <T> ScheduledFuture<Optional<T>> schedule(final Callable<T> task, long delay, TimeUnit timeUnit){
		return this.getScheduleExecutor().schedule(new Callable<Optional<T>>() {

			@Override
			public Optional<T> call() throws Exception {
				try{
					return Optional.of(task.call());
				}catch(Throwable t){
					log.error(errorLogContent, t);
					return Optional.absent();
				}finally{
					taskCount.decrementAndGet();
				}
			}
		}, delay, timeUnit);
	}

	private ScheduledExecutorService getScheduleExecutor() {
		return (ScheduledExecutorService) this.executor;
	}

}
