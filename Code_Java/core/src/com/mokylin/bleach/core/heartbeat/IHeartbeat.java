package com.mokylin.bleach.core.heartbeat;
/**
 * 心跳任务
 * 
 * <p>所有每次心跳都要执行一遍的任务必须实现此接口（以便统一，如果有更好的方法欢迎讨论）
 * 
 * @author yaguang.xiao
 *
 */

public interface IHeartbeat {
	
	/**
	 * 每次心跳(每帧)都会执行的方法
	 */
	public void heartbeat();
}
