package com.mokylin.bleach.core.event;


/**
 * 事件监听器
 * 
 * <P>
 * @author yaguang.xiao
 *
 * @param <T> 需要监听的事件类
 */
public interface IEventListener<T> {

	/**
	 * 发生事件时调用该方法
	 * @param event
	 */
	void onEventOccur(T event);
}
