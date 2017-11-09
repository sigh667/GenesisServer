package com.mokylin.bleach.gameserver.human.event;

import com.mokylin.bleach.gameserver.human.Human;

/**
 * Human等级提升事件
 * 
 * @author ChangXiao
 *
 */
public class HumanLevelUpEvent {
	public final Human human;
	public final int originLevel;
	public final HumanLevelUpReason reason;

	public HumanLevelUpEvent(Human human, HumanLevelUpReason reason, int originLevel) {
		this.human = human;
		this.reason = reason;
		this.originLevel = originLevel;
	}

}
