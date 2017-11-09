package com.mokylin.bleach.gameserver.currency.event;

import com.mokylin.bleach.gameserver.human.Human;

/**
 * 添加充值钻石的事件
 * @author yaguang.xiao
 *
 */
public class AddChargeDiamondEvent {

	public final long addValue;
	public final Human human;
	
	public AddChargeDiamondEvent(long addValue, Human human) {
		this.addValue = addValue;
		this.human = human;
	}
	
}
