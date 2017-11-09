package com.mokylin.bleach.core.redis;


public interface IRedisResponse<T> {

	/**
	 * 获取执行的结果，如果为null则直接返回null。
	 * 
	 * @return
	 */
	public T get();
	
	/**
	 * 如果执行结果不为null，获取执行结果，如果为null，返回默认值。
	 * 
	 * @param defaultValue 默认值
	 * @return
	 */
	public T getOr(T defaultValue);
	
	/**
	 * 获取错误消息，如果没有，返回空字符串。
	 * 
	 * @return
	 */
	public String errorMsg();
	
	/**
	 * 是否成功。
	 * 
	 * @return
	 */
	public boolean isSuccess();
}
