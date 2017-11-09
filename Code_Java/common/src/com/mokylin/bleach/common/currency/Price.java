package com.mokylin.bleach.common.currency;


/**
 * 价格
 * @author yaguang.xiao
 *
 */
public class Price {

	/** 货币类型 */
	public final Currency currency;
	/** 价格 */
	public final long price;
	
	public Price(Currency currency, long price) {
		this.currency = currency;
		this.price = price;
	}
}
