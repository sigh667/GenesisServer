package com.mokylin.bleach.common.prop;

/**
 * 属性范围
 * @author yaguang.xiao
 *
 */
public class PropScope {

	private final long minValue;
	private final long maxValue;

	public PropScope(long minValue, long maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public long getMinValue() {
		return minValue;
	}

	public long getMaxValue() {
		return maxValue;
	}
	
	/**
	 * 检查值是否有效，如果无效则抛异常
	 * 
	 * @param value
	 */
	public void checkValid(long value) {
		if (value < minValue || value > maxValue) {
			throw new IllegalArgumentException("Invalid Value : " + value
					+ " for " + this + "![minValue:" + minValue + ", maxValue:"
					+ maxValue + "]");
		}
	}

}
