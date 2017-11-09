package com.mokylin.bleach.gameserver.human.vip.event;

import com.mokylin.bleach.gameserver.human.Human;

/**
 * VIP等级提升事件
 * 
 * @author ChangXiao
 *
 */
public class VipLevelUpEvent {
	public final Human human;

	public VipLevelUpEvent(Human human) {
		this.human = human;
	}

}
