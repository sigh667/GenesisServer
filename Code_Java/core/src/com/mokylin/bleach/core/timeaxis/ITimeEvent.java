package com.mokylin.bleach.core.timeaxis;


/**
 * 时间轴事件
 * 
 * <p>在时间轴上定期执行的事件<br>
 * 每一个需要被时间轴{@link TimeAxis}调度的事件必须实现此接口
 * <p><h3>时间轴{@link TimeAxis}只接受此类型的事件</h3>
 * 
 * @author yaguang.xiao
 *
 * @param <T>	宿主的类型
 */

public interface ITimeEvent<T> {
	
	/**
	 * 获取时间轴事件的类型
	 * @return
	 */
	public ITimeEventType getEventType();

	/**
	 * 获取时间轴事件的Id
	 * @return
	 */
	public long getEventId();

	/**
	 * 任务发生时调用的方法
	 * @param timeAxisHost	时间轴的宿主
	 */
	public void eventOccur(T timeAxisHost);
}
