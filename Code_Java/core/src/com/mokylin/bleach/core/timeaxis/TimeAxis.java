package com.mokylin.bleach.core.timeaxis;

import com.mokylin.bleach.core.annotation.NotThreadSafe;
import com.mokylin.bleach.core.heartbeat.IHeartbeat;
import com.mokylin.bleach.core.time.TimeService;

/**
 * 时间轴
 * 
 * <p>此类的每一个对象都是一个独立的时间轴<br>
 * 它可以在指定的时间或者定期执行时间轴事件{@link ITimeEvent}<br>
 * 此类不会接受null事件的调度
 * 
 * <p>此类提供了时间轴事件的注册和删除方法，由使用者自行调用
 * 
 * <p>例子：<br>
 * 	Human human = new Human(); // 创建Human<br>
 * 	TimeAxis<Human> timeAxis = new TimeAxis<Human>(human); // 创建时间轴，宿主为Human<br>
 * 	ITimeEvent<Human> task = new SimpleTimeAxisEventImpl(); // 创建时间轴事件<br>
 *  timeAxis.scheduleTaskAfterMS(task, 1000, 1000);	// 调度时间轴事件，一秒之后 执行一次，之后每隔一秒执行一次
 * 
 * @author yaguang.xiao
 *
 * @param <T>	时间轴的宿主的类型
 */

@NotThreadSafe
public class TimeAxis<T> implements IHeartbeat {
	
	/** 任务队列 */
	private TaskPriorityQueue<T> taskQueue = new TaskPriorityQueue<T>();
	/** 时间轴的宿主 */
	private T timeAxisHost;
	/** 时间服务 */
	private TimeService timeService;
	
	/**
	 * 构造时间轴
	 * @param timeAxisHost	时间轴的宿主
	 */
	public TimeAxis(TimeService timeService, T timeAxisHost) {
		this.timeAxisHost = timeAxisHost;
		this.timeService = timeService;
	}

	@Override
	public void heartbeat() {
		while(!this.taskQueue.isEmpty()) {
 			TimeAxisTask<T> task = this.taskQueue.peek();	// 队列中不可能有null元素
			
 			if(task.isCancel()) {	// 任务已被取消
 				this.taskQueue.poll();
 				continue;
 			}
 			
			if(timeService.isTimeUp(task.getOccurrenceTime())) {// 该任务已经到时间需要执行了
				this.taskQueue.poll();
				
				try {
					task.getTimeEvent().eventOccur(timeAxisHost);// 指定事件
				} catch (Exception e) {
					// TODO 记录错误日志
				}
				
				if(task.isPeriodTask()) {//该任务是周期任务，则重新调度任务
					task.nextPeriod();
					this.scheduleTask(task);
				}
			} else {// 该任务没有到时间，则后面的任务都不会到时间
				break;
			}
		}
	}
	
	/**
	 * 调度在指定时间执行的事件（非周期事件）
	 * @param timeEvent	事件
	 * @param occurTime	发生时间
	 * @return	是否调度成功
	 */
	public boolean scheduleEventOnThisTime(ITimeEvent<T> timeEvent, long occurTime) {
		return this.scheduleEventOnThisTime(timeEvent, occurTime, -1);
	}
	
	/**
	 * 调度在指定时间执行的事件
	 * @param timeEvent	事件
	 * @param occurTime	发生时间
	 * @param period	周期
	 * @return	是否调度成功
	 */
	public boolean scheduleEventOnThisTime(ITimeEvent<T> timeEvent, long occurTime, long period) {
		if(timeEvent == null)
			return false;
		
		TimeAxisTask<T> task = new TimeAxisTask<T>(timeEvent);
		task.setOccurrenceTime(occurTime);
		task.setPeriod(period);
		return this.scheduleTask(task);
	}
	
	/**
	 * 调度一定时间后指定的事件（非周期事件）
	 * @param timeEvent	时间轴事件
	 * @param ms	多少毫秒之后
	 * @return	是否调度成功
	 */
	public boolean scheduleEventAfterMS(ITimeEvent<T> timeEvent, long ms) {
		return this.scheduleEventAfterMS(timeEvent, ms, -1);
	}
	
	/**
	 * 调度一定时间后执行的事件
	 * @param timeEvent	时间轴事件
	 * @param ms	多少毫秒之后
	 * @param period	周期
	 * @return
	 */
	public boolean scheduleEventAfterMS(ITimeEvent<T> timeEvent, long ms, long period) {
		if(timeEvent == null || ms < 0)
			return false;
		
		long now = timeService.now();
		
		TimeAxisTask<T> task = new TimeAxisTask<T>(timeEvent);
		task.setOccurrenceTime(now + ms);
		task.setPeriod(period);
		return this.scheduleTask(task);
	}
	
	/**
	 * 调度时间轴任务
	 * @param task
	 */
	private boolean scheduleTask(TimeAxisTask<T> task) {
		if(task == null)
			return false;
		
		this.taskQueue.add(task);
		return true;
	}
	
	/**
	 * 根据时间轴事件的类型和Id来删除事件{@link ITimeEvent}
	 * @param type
	 * @param eventId
	 */
	public void del(ITimeEventType type, long eventId) {
		this.taskQueue.del(type, eventId);
	}
	
	/**
	 * 根据时间轴事件的类型来删除事件{@link ITimeEvent}
	 * @param type
	 */
	public void del(ITimeEventType type) {
		this.taskQueue.del(type);
	}
}
