package com.mokylin.bleach.common.currency;

import com.mokylin.bleach.core.util.arr.EnumArray;

/**
 * 金钱容器的基类
 * @author baoliang.shen
 *
 */
public class CurrencyContainer {

	/** 金币属性管理器 */
	protected EnumArray<CurrencyPropId, Long> currencyProp = EnumArray.create(CurrencyPropId.class, Long.class, 0L);

	/**
	 * 获取指定属性的值
	 * @param currencyPropId
	 * @return
	 */
	public long get(CurrencyPropId currencyPropId) {
		return this.currencyProp.get(currencyPropId);
	}
	/**
	 * 设置指定属性的值
	 * @param currencyPropId
	 * @param value
	 */
	public void set(CurrencyPropId currencyPropId, long value) {
		this.currencyProp.set(currencyPropId, value);
	}

	/**
	 * 判断钱是否够用（此方法不能用来判断钻石）
	 * @param currencyPropId	金钱类型
	 * @param value		金钱数量
	 * @return	true 够用		false 不够用
	 */
	public boolean isMoneyEnough(CurrencyPropId currencyPropId, long value) {
		if(currencyPropId == null || currencyPropId == CurrencyPropId.CHARGE_DIAMOND || currencyPropId == CurrencyPropId.FREE_DIAMOND || value <= 0) {
			throw new IllegalArgumentException(String.format("参数有误：value=%d (value必须大于0), currency=%s (currency不能为空，且不能是钻石)", value, currencyPropId));
		}

		long curMoney = this.currencyProp.get(currencyPropId);
		return curMoney >= value;
	}

	/**
	 * 判断钻石是否够用
	 * @param value	钻石数量
	 * @return	true 够用		false 不够用
	 */
	public boolean isDiamondEnough(long value) {
		if(value <= 0) {
			throw new IllegalArgumentException(String.format("参数有误：value=%d (value必须大于0)", value));
		}

		long curChargeDiamond = this.currencyProp.get(CurrencyPropId.CHARGE_DIAMOND);
		long curFreeDiamond = this.currencyProp.get(CurrencyPropId.FREE_DIAMOND);

		return curChargeDiamond + curFreeDiamond >= value;
	}
}
