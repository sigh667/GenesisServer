package com.mokylin.bleach.core.timeaxis;

import java.util.Iterator;
import java.util.PriorityQueue;

import com.mokylin.bleach.core.annotation.NotThreadSafe;

/**
 * 任务优先级队列
 * 
 * <p>用来存放时间轴上的时间轴任务{@link TimeAxisTask}
 * 
 * @author yaguang.xiao
 * 
 * @param <T>
 *            时间轴宿主类型
 */

@NotThreadSafe
class TaskPriorityQueue<T> extends PriorityQueue<TimeAxisTask<T>> {
	private static final long serialVersionUID = 1L;

	/**
	 * 删除指定类型的时间轴任务
	 * 
	 * @param type
	 *            时间轴任务类型
	 */
	public void del(ITimeEventType type) {
		Iterator<TimeAxisTask<T>> it = this.iterator();
		while (it.hasNext()) {
			TimeAxisTask<T> task = it.next();
			if (task != null && task.getTimeEvent().getEventType().equals(type)) {
				task.cancel();
			}
		}
	}

	/**
	 * 删除指定类型，指定Id的时间轴任务
	 * 
	 * @param type
	 *            时间轴任务类型
	 * @param eventId
	 *            时间轴任务Id
	 */
	public void del(ITimeEventType type, long eventId) {
		Iterator<TimeAxisTask<T>> it = this.iterator();
		while (it.hasNext()) {
			TimeAxisTask<T> task = it.next();
			if (task != null && task.getTimeEvent().getEventType().equals(type)
					&& task.getTimeEvent().getEventId() == eventId) {
				task.cancel();
			}
		}
	}
}
