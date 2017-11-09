package com.mokylin.bleach.gameserver.core.timeevent;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.mokylin.bleach.core.timeaxis.ITimeEvent;
import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.gameserver.core.global.Globals;

public class TimeEventService {
	
	/**
	 * 将一个事件添加到当天对应的周期时间点上（以timeEventId对应的{@link TimeEventTemplate}
	 * 中定义的时间点为基准，period为周期）。如果指定的时间点未在数据表中配置，则注册失败。<p>
	 * 
	 * <b>注：使用该方法注册的事件，该方法会从当前时间开始计算，在最早的那个满足周期要求的时间点执行，并按周期为period执行。
	 * 即如果调用方法的时间点已经过了当天的Event定义的时间点，就加一个周期，如果还是过期就再加一个周期，直到不过期，就从那里开始执行</b>
	 * 
	 * @param timeAxis
	 * @param now 当前时间
	 * @param time
	 * @param period 执行周期
	 * @param event 
	 * @param timeEventId 触发的时间ID，
	 */
	public <T> void startPeriodEventInRecentEventTime(TimeAxis<T> timeAxis, long now, LocalTime time, long period, ITimeEvent<T> event){
		long startTime = getEventTimeOfToday(now, time.getMillisOfDay());
		
		startPeriodEventInRecentEventTime(timeAxis, now, startTime, period, event);
	}
	
	/**
	 * 获取现在时间点所在那天的Event时间点。<p>
	 * 
	 * 注意，这个方法中根据的日期是自然日期，比如，Event时间点是3:00， 而现在是3月3日2:00，
	 * 则该方法返回的Event时间点就是3月3日3:00。
	 * @param now 当前时间
	 * @param millisOfDay
	 * 
	 * @return
	 */
	private long getEventTimeOfToday(long now, int millisOfDay) {
		return new DateTime(Globals.getTimeService().now()).withMillisOfDay(millisOfDay).getMillis();
	}

	/**
	 * 将一个事件添加到当天对应的周期时间点上（以startTime为基准，period为周期）。<p>
	 * 
	 * <b>注：使用该方法注册的事件，最早会在从当前时间开始计算，满足周期要求的时间点执行，并按周期为period执行。
	 * 即如果调用方法的时间点已经过了当天的Event定义的时间点，就加一个周期，如果还是过期就再加一个周期，直到不过期，就从那里开始执行</b>
	 * 
	 * @param timeAxis
	 * @param now TODO
	 * @param startTime the milliseconds from 1970-01-01T00:00:00Z
	 * @param period 执行周期
	 * @param event 
	 */
	public <T> void startPeriodEventInRecentEventTime(TimeAxis<T> timeAxis, long now, long startTime, long period, ITimeEvent<T> event){
		if (event==null) 
			return;

		long todayNow = Globals.getTimeService().now();	
		while(startTime > todayNow) {
			startTime -= period;
		}
		while(startTime < todayNow) {
			startTime += period;
		}
		
		timeAxis.scheduleEventOnThisTime(event, startTime, period);
	}
}
