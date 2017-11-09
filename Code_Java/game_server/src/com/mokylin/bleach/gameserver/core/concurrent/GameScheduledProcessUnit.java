package com.mokylin.bleach.gameserver.core.concurrent;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.ExecutionContextExecutorService;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Scheduler;

import com.mokylin.bleach.core.akka.Akka;
import com.mokylin.bleach.core.timeaxis.TimeAxis;

/**
 * 用于进行计时操作的定时器。<p>
 * 
 * 和逻辑线程一样，可以向该定时器提交task的同时，指定该task在多长时间后执行。
 * 需要注意的时候，如果使用了没有指定时间的方法，则会认为提交的task需要
 * 立即执行。<p>
 * 
 * <b>注意：</b>提交的任务中可能抛出Exception，该定时器将忽略这些Exception继续执行，
 * 只会将Exception的信息输出的日志中。另外，该定时器中提交的任务在执行的时候是并行执行的。
 * 
 * @author pangchong
 *
 */
public class GameScheduledProcessUnit extends GameProcessUnit{
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final Scheduler scheduler;
	
	GameScheduledProcessUnit(String threadName, ExecutionContextExecutorService pool, Akka akka) {
		super(threadName, pool);
		this.scheduler = akka.getActorSystem().scheduler();
	}
	
	/**
	 * 指定task在initialDelay时间后执行，然后每隔interval的时间后重复执行。<p>
	 * 
	 * 不是特别推荐使用该方法，游戏服务器现在是多线程的模型，所以如果需要定时，优先推荐
	 * 使用时间轴{@link TimeAxis}，其次，推荐使用定时发消息的方式{@link this#scheduleOnce(ActorRef, Object, long, TimeUnit)}。
	 * 
	 * @param initialDelay
	 * @param interval
	 * @param task
	 */
	public void schedule(FiniteDuration initialDelay, FiniteDuration interval, final Runnable task){
		this.scheduler.schedule(initialDelay, interval, new Runnable() {
			@Override
			public void run() {
				try{
					task.run();
				}catch(Throwable t){
					log.error(errorLogContent, t);
				}
			}
		}, this.getExecutionContext());
	}

	/**
	 * 指定消息msg在delay时间后（单位TimeUnit）被发送到executeActor。
	 * 
	 * @param executeActor
	 * @param msg
	 * @param delay
	 * @param timeUnit
	 * @return
	 */
	public Cancellable scheduleOnce(ActorRef executeActor, Object msg, long delay, TimeUnit timeUnit) {
		return this.scheduler.scheduleOnce(FiniteDuration.create(delay, timeUnit), executeActor, msg, this.getExecutionContext(), ActorRef.noSender());
	}
}
