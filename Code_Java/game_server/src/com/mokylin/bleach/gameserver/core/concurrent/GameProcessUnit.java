package com.mokylin.bleach.gameserver.core.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContextExecutorService;
import scala.concurrent.Future;
import akka.dispatch.Futures;

/**
 * 逻辑处理器。<p>
 * 
 * 该处理逻辑只负责执行提交到该GameProcessUnit中的任务。<br>
 * 任务的分派和提交由具体使用该处理逻辑的地方自行调用。<br>
 * <br>
 * <b>注意：</b>提交的任务中可能抛出Exception，该处理器将忽略这些Exception继续执行，
 * 只会将Exception的信息输出到日志中。
 * 
 * @author pangchong
 *
 */
public class GameProcessUnit{
	
	/** 日志 */
	private static final Logger log = LoggerFactory.getLogger(GameProcessUnit.class);
	/** 线程池（有线程自动恢复功能） */
	final ExecutionContextExecutorService executor;
	/** 线程名 */
	final String threadName;
	/** 异常错误日志 */
	final String errorLogContent;
	
	final AtomicLong taskCount = new AtomicLong(0);
	
	private final AsyncArgs args = new AsyncArgs();
	
	GameProcessUnit(final String threadName, final ExecutionContextExecutorService pool) {
		this.threadName = threadName;
		this.errorLogContent = this.threadName + " Thread arises exception";
		executor = pool;
	}
	
	/**
	 * 提交不带有返回值的处理。<p>
	 * 
	 * @param task
	 * @return 
	 */
	public Future<Void> submitTask(final ArgsRunnable task){
		taskCount.incrementAndGet();
		return Futures.future(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				try{
					task.run(args);
				}catch(Throwable e){
					log.error(errorLogContent, e);
					throw e;
				}finally{
					taskCount.decrementAndGet();
				}
				return null;
			}
		}, executor);
	}
	
	/**
	 * 提交带有返回值的处理。<p>
	 * 
	 * @param task
	 * @return
	 */
	public <T> Future<T> submitTask(final ArgsCallable<T> task){
		taskCount.incrementAndGet();
		return Futures.future(new Callable<T>(){
			@Override
			public T call() throws Exception {
				try{
					return task.call(args);
				}catch(Throwable e){
					log.error(errorLogContent, e);
					throw e;
				}finally{
					taskCount.decrementAndGet();
				}
			}
			
		}, executor);
	}
	
	/**
	 * 获取该处理器所使用的线程池。
	 * 
	 * @return
	 */
	public ExecutionContext getExecutionContext(){
		return this.executor;
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
