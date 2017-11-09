package com.mokylin.bleach.core.timeaxis;

import com.mokylin.bleach.core.annotation.NotThreadSafe;

/**
 * 时间轴任务
 * 
 * <p>可以被时间轴调度的任务
 * <p><h3>每一个被调度的事件都会被封装成此对象注册到时间轴{@link TimeAxis}中</h3>
 * 这样做的目的是封装不需要被外部看到的信息比如：任务发生时间、周期、任务取消标志的修改方法
 * 
 * @author yaguang.xiao
 *
 * @param <T>	时间轴的宿主类型
 * 
 * @see TimeAxis
 */

@NotThreadSafe
class TimeAxisTask<T> implements Comparable<TimeAxisTask<?>> {
	
	/** 任务发生时间 */
	private long occurrenceTime;
	/** 周期时间(负数代表非周期任务) */
	private long period = -1;
	/** 任务是否被取消了 */
	private boolean cancel = false;
	/** 时间事件 */
	private ITimeEvent<T> timeEvent;
	
	public TimeAxisTask(ITimeEvent<T> timeEvent) {
		this.timeEvent = timeEvent;
	}
	
	/**
	 * 获取时间事件
	 * @return
	 */
	public ITimeEvent<T> getTimeEvent() {
		return this.timeEvent;
	}
	
	/**
	 * 获取任务发生时间
	 * @return
	 */
	public long getOccurrenceTime() {
		return this.occurrenceTime;
	}
	
	/**
	 * 设置该任务的发生时间
	 * @param occurrenceTime
	 */
	public void setOccurrenceTime(long occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}
	
	/**
	 * 判断该任务是不是周期任务
	 * @return
	 */
	public boolean isPeriodTask() {
		return this.period > 0;
	}
	
	/**
	 * 使该任务进入下一个周期
	 */
	public void nextPeriod() {
		if(!this.isPeriodTask())
			return;
		
		this.occurrenceTime += this.period;
	}
	
	/**
	 * 获取发生周期(若为负数，则不是周期任务)
	 * @return
	 */
	public long getPeriod() {
		return this.period;
	}
	
	/**
	 * 设置发生周期
	 * @param period
	 */
	public void setPeriod(long period) {
		this.period = period;
	}
	
	/**
	 * 取消该任务
	 */
	public void cancel() {
		this.cancel = true;
	}
	
	/**
	 * 该任务是否已经取消
	 * @return
	 */
	public boolean isCancel() {
		return this.cancel;
	}

	@Override
	public int compareTo(TimeAxisTask<?> o) {
		if(this.occurrenceTime < o.occurrenceTime)
			return -1;
		else if (this.occurrenceTime > o.occurrenceTime)
			return 1;
		
		return 0;
	}
}
