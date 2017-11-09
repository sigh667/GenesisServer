package com.mokylin.bleach.common.shop;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalTime;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.currency.Price;
import com.mokylin.bleach.common.dailyrefresh.DailyTaskType;
import com.mokylin.bleach.common.function.FunctionType;
import com.mokylin.bleach.common.shop.template.ShopItemPriceTemplate;
import com.mokylin.bleach.common.window.Window;

/**
 * 商店类型
 * @author yaguang.xiao
 *
 */
public enum ShopType {

	/** 普通商店 */
	GENERAL(0, Window.GENERAL_SHOP, FunctionType.GENERAL_SHOP, true, DailyTaskType.GENERAL_SHOP_AUTO_REFRESH, DailyTaskType.GENERAL_SHOP_REFRESH_COUNTS_RESET, Currency.GOLD, Currency.DIAMOND),
	/** 神秘商店 */
	MYSTERIOUS(1, Window.MYSTERIOUS_SHOP, FunctionType.MYSTERIOUS_SHOP, false, DailyTaskType.MYSTERIOUS_SHOP_AUTO_REFRESH, DailyTaskType.MYSTERIOUS_SHOP_REFRESH_COUNTS_RESET , Currency.GOLD, Currency.DIAMOND),
	;
	
	/** 商店类型数字标识 */
	private final int index;
	/** 货币列白哦 */
	private final List<Integer> currencyList;
	/** 商店对应的窗口 */
	private final Window window;
	private final boolean isOpenForever;
	/** 功能类型 */
	private final FunctionType funcType;
	
	private final DailyTaskType autoRefreshType;
	private final DailyTaskType autoResetManuallyRefreshTimeType;
	
	ShopType(int index, Window window, FunctionType funcType, boolean isOpenForever, DailyTaskType autoRefreshType, DailyTaskType autoResetManuallyRefreshTimeType, Currency... shopCurrencyTypeArr) {
		this.index = index;
		List<Integer> currencyList = Lists.newArrayListWithCapacity(shopCurrencyTypeArr.length);
		for(Currency currencyType : shopCurrencyTypeArr) {
			currencyList.add(currencyType.getIndex());
		}
		this.currencyList = Collections.unmodifiableList(currencyList);
		this.window = window;
		this.funcType = funcType;
		this.isOpenForever = isOpenForever;
		this.autoRefreshType = autoRefreshType;
		this.autoResetManuallyRefreshTimeType = autoResetManuallyRefreshTimeType;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public Window getWindow() {
		return this.window;
	}
	
	/**
	 * 获取物品的价格
	 * @param itemTmplId
	 * @param num
	 * @return
	 */
	public Price getPrice(int itemTmplId, int num) {
		ShopItemPriceTemplate itemPriceTmpl = GlobalData.getTemplateService().get(itemTmplId, ShopItemPriceTemplate.class);
		if(itemPriceTmpl == null) {
			throw new RuntimeException(String.format("物品【%d】没有对应的价格配置！", itemTmplId));
		}

		return new Price(itemPriceTmpl.getGeneralShopCurrencyType(), itemPriceTmpl.getGeneralShopPrice() * num);
	}
	
	private static Map<Integer, ShopType> values = Maps.newHashMap();
	static {
		for(ShopType type : ShopType.values()) {
			values.put(type.getIndex(), type);
		}
	}
	
	/**
	 * 根据数字标识获取ShopType
	 * @param index
	 * @return
	 */
	public static ShopType getByIndex(int index) {
		return values.get(index);
	}
	
	/**
	 * 判断指定数字标识是否有效
	 * @param index
	 * @return
	 */
	public static boolean isValid(int index) {
		return values.containsKey(index);
	}
	
	/**
	 * 获取商店货币列表
	 * @return
	 */
	public List<Integer> getCurrencyList() {
		return this.currencyList;
	}
	
	/**
	 * 是否永久开放
	 * @return
	 */
	public boolean isOpenForever() {
		return this.isOpenForever;
	}
	
	/**
	 * 获取功能类型
	 * @return
	 */
	public FunctionType getFuncType() {
		return this.funcType;
	}
	
	/**
	 * 获取每日刷新任务枚举类型
	 * @return
	 */
	public List<LocalTime> getAutoRefreshTimeList() {
		return this.autoRefreshType.getTimeList();
	}
	
	public LocalTime getAutoResetManuallyRefreshTime() {
		return this.autoResetManuallyRefreshTimeType.getSingleTime();
	}

}
