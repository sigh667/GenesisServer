package com.mokylin.bleach.core.concurrent.process;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * 逻辑处理线程。<p>
 * 
 * 该处理逻辑只负责执行提交到该ProcessUnit中的任务。<br>
 * 任务的分派和提交由具体使用该处理逻辑的地方自行调用。<br>
 * <br>
 * <b>注意：</b>提交的任务中可能抛出Exception，该处理器将忽略这些Exception继续执行，
 * 只会将Exception的信息输出的日志中。
 * 
 * @author pangchong
 *
 */
public class ProcessUnit{
	
	/** 日志 */
	private static final Logger log = LoggerFactory.getLogger(ProcessUnit.class);
	/** 线程池（有线程自动恢复功能） */
	final ExecutorService executor;
	/** 线程名 */
	final String threadName;
	/** 异常错误日志 */
	final String errorLogContent;
	
	final AtomicLong taskCount = new AtomicLong(0);
	
	ProcessUnit(final String threadName, final ExecutorService pool) {
		this.threadName = threadName;
		this.errorLogContent = this.threadName + " Thread arises exception";
		executor = pool;
	}
	
	/**
	 * 获取单线程池
	 * @return
	 */
	public ExecutorService getExecutor() {
		return this.executor;
	}
	
	/**
	 * 提交不带有返回值的处理。<p>
	 * 
	 * @param task
	 * @return 
	 */
	public Future<?> submitTask(final Runnable task){
		taskCount.incrementAndGet();
		return executor.submit(new Runnable() {
			
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
		});
	}
	
	/**
	 * 提交带有返回值的处理。<p>
	 * 
	 * @param task
	 * @return
	 */
	public <T> Future<Optional<T>> submitTask(final Callable<T> task){
		taskCount.incrementAndGet();
		return executor.submit(new Callable<Optional<T>>() {

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
		});
	}
	
	/**
	 * 获取提交但仍没有执行完毕的任务数量。
	 * 
	 * @return
	 */
	public long getUnDoneTaskCount(){
		return this.taskCount.get();
	}
	
	/**
	 * 关闭执行的线程。
	 */
	public void shutdown(){
		this.executor.shutdown();
	}

	public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		this.executor.awaitTermination(timeout, unit);
	}
}
