package com.mokylin.bleach.robot.currency;

import java.util.List;

import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.core.util.arr.EnumArray;
import com.mokylin.bleach.robot.human.Human;

/**
 * 金钱管理器，管理一些金钱相关的逻辑
 * @author baoliang.shen
 *
 */
public class CurrencyManager{

	private Human human;
	
	/** 金币属性管理器 */
	protected EnumArray<Currency, Long> currencyProp = EnumArray.create(Currency.class, Long.class, 0L);

	public CurrencyManager(Human human) {
		this.human = human;
	}
	public Human getHuman() {
		return human;
	}

	public void load(List<Long> currencyPropList) {
		if (currencyPropList==null || currencyPropList.isEmpty())
			return;

		Currency[] currencyIds = Currency.values();
		if (currencyIds.length!=currencyPropList.size()) {
			String error = String.format("Currency count==[%d],msg's currencyPropList count==[%d],not equal!",
					currencyIds.length, currencyPropList.size());
			throw new RuntimeException(error);
		}

		for (int i = 0; i < currencyPropList.size(); i++) {
			this.set(currencyIds[i], currencyPropList.get(i));
		}
	}

	/**
	 * 获取指定属性的值
	 * @param currency
	 * @return
	 */
	public long get(Currency currency) {
		return this.currencyProp.get(currency);
	}
	/**
	 * 设置指定属性的值
	 * @param currency
	 * @param value
	 */
	public void set(Currency currency, long value) {
		this.currencyProp.set(currency, value);
	}

	/**
	 * 判断钱是否够用（此方法不能用来判断钻石）
	 * @param currency	金钱类型
	 * @param value		金钱数量
	 * @return	true 够用		false 不够用
	 */
	public boolean isMoneyEnough(Currency currency, long value) {
		if(currency == null || value <= 0) {
			throw new IllegalArgumentException(String.format("参数有误：value=%d (value必须大于0), currency=%s (currency不能为空)", value, currency));
		}

		long curMoney = this.currencyProp.get(currency);
		return curMoney >= value;
	}
}
