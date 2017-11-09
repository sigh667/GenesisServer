package com.mokylin.bleach.gameserver.core.autoexecutetask;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.google.common.collect.Lists;
import com.mokylin.bleach.core.timeaxis.ITimeEvent;
import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.core.util.TimeUtils;
import com.mokylin.bleach.gameserver.core.global.Globals;

/**
 * 自动执行的任务调度
 * 
 * @author yaguang.xiao
 * 
 */
public abstract class AbstractAutoExecuteTask<H> implements ITimeEvent<H> {

	private final List<LocalTime> autoExecuteTimeList;
	private final TimeAxis<H> timeAxis;

	protected AbstractAutoExecuteTask(LocalTime autoExecuteTime,
			TimeAxis<H> timeAxis) {
		this.autoExecuteTimeList = Lists.newArrayList(autoExecuteTime);
		this.timeAxis = timeAxis;
	}

	protected AbstractAutoExecuteTask(List<LocalTime> autoExecuteTimeList,
			TimeAxis<H> timeAxis) {
		this.autoExecuteTimeList = autoExecuteTimeList;
		this.timeAxis = timeAxis;
	}

	/**
	 * 开始调度任务，调度完成之后会自动调用triggerExecute方法
	 */
	public final void start(H host) {
		this.startNextSchedule();
		this.triggerExecute(host);
	}

	@Override
	public final void eventOccur(H host) {
		this.autoExecute(host);

		this.startNextSchedule();
	}

	/**
	 * 进行下次任务的调度
	 */
	private void startNextSchedule() {
		long nextAutoExecuteTime = this.getNextAutoExecuteTime();
		this.timeAxis.scheduleEventOnThisTime(this, nextAutoExecuteTime);
	}

	/**
	 * 获取下次自动刷新的时间点
	 * 
	 * @return
	 */
	private long getNextAutoExecuteTime() {
		long todayNow = Globals.getTimeService().now();

		for (LocalTime autoExecuteTime : autoExecuteTimeList) {
			long eventTimeOfToday = new DateTime(todayNow).withMillisOfDay(
					autoExecuteTime.getMillisOfDay()).getMillis();
			if (todayNow < eventTimeOfToday) {
				return eventTimeOfToday;
			}
		}

		// 明天的第一个自动刷新时间刷新货物
		return new DateTime(todayNow).withMillisOfDay(
				autoExecuteTimeList.get(0).getMillisOfDay()).getMillis()
				+ TimeUtils.DAY;
	}

	/**
	 * 判断是任务是不是需要被执行
	 * 
	 * @return
	 */
	protected final boolean isNeedExecute() {
		long now = Globals.getTimeService().now();
		for (LocalTime autoExecuteTime : autoExecuteTimeList) {
			if (TimeUtils.canDoDailyResetOp(now, this.getLastExecuteTime(),
					autoExecuteTime)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 获取距离下次自动执行的时间长度
	 * 
	 * @return
	 */
	public final int getSecondsBeforeNextAutoExecuteTime() {
		return (int) ((this.getNextAutoExecuteTime() - Globals.getTimeService()
				.now()) / 1000);
	}

	/**
	 * 获取上次执行的时间
	 * 
	 * @return
	 */
	public abstract long getLastExecuteTime();

	/**
	 * 触发执行，由开发者自己决定什么时候调用此方法
	 * 
	 * @param host
	 */
	protected abstract void triggerExecute(H host);

	/**
	 * 自动执行，在自动执行时间点执行的方法
	 * 
	 * @param host
	 */
	protected abstract void autoExecute(H host);

}
