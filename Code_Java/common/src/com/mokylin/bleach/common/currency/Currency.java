package com.mokylin.bleach.common.currency;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 货币类型
 * @author yaguang.xiao
 *
 */
public enum Currency {
	
	/** 金币 */
	GOLD(0, CurrencyPropId.GOLD),
	/** 钻石 */
	DIAMOND(1, null) {
		@Override
		public boolean isDiamond() {
			return true;
		}
	},
	;
	
	private final int index;
	private final CurrencyPropId currencyPropId;
	
	Currency(int index, CurrencyPropId currency) {
		this.index = index;
		this.currencyPropId = currency;
	}
	
	/**
	 * 获取该枚举对应的数字标识
	 * @return
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * 获取货币枚举
	 * @return
	 */
	public CurrencyPropId getCurrencyPropId() {
		return this.currencyPropId;
	}
	
	/**
	 * 是否是钻石
	 * @return
	 */
	public boolean isDiamond() {
		return false;
	}
	
	private static Map<Integer, Currency> values = Maps.newHashMap();
	static {
		for(Currency value : Currency.values()) {
			values.put(value.getIndex(), value);
		}
	}
	
	/**
	 * 根据数字标识获取对应的枚举
	 * @param index
	 * @return
	 */
	public static Currency getByIndex(int index) {
		return values.get(index);
	}
	
	/**
	 * 判断指定的数字标识是否有效
	 * @param index
	 * @return
	 */
	public static boolean isValid(int index) {
		return values.containsKey(index);
	}
}
