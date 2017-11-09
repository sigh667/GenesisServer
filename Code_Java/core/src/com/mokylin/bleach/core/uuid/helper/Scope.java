package com.mokylin.bleach.core.uuid.helper;

/**
 * 范围
 * @author Administrator
 *
 */
public final class Scope {

	/** 最小值 */
	public final long minValue;
	/** 最大值 */
	public final long maxValue;
	
	/**
	 * 创建范围
	 * @param minValue	最小值
	 * @param maxValue	最大值
	 */
	public Scope(long minValue, long maxValue) {
		if(minValue > maxValue) {
			throw new RuntimeException("minValue:" + minValue + " cannot be larger than maxValue:" + maxValue);
		}
		
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
}
