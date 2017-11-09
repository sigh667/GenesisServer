package com.mokylin.bleach.gameserver.currency.event;

import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 给钱成功事件
 * @author yaguang.xiao
 *
 */
public class GiveMoneySuccessEvent {
	
	/** 玩家对象 */
	public final Human human;
	/** 金钱类型 */
	public final CurrencyPropId currency;
	/** 金钱数量 */
	public final long giveValue;
	
	public GiveMoneySuccessEvent(Human human, CurrencyPropId currency, long giveValue) {
		this.human = human;
		this.currency = currency;
		this.giveValue = giveValue;
	}
}
