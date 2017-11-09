package com.mokylin.bleach.gameserver.human.event;

import com.mokylin.bleach.core.event.IEventListener;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 记录玩家升级日志监听类
 * @author ChangXiao
 *
 */
public class HumanLevelUpLogListener implements IEventListener<HumanLevelUpEvent>{

	@Override
	public void onEventOccur(HumanLevelUpEvent event) {
		Human human = event.human;
		//运营日志-记录角色升级日志
		String iEventId = human.getServerGlobals().genLogEventId();
		human.getServerGlobals().getLogService().logRoleLevelUp(human, iEventId, event.reason.ordinal(), event.originLevel);
	}
	
}
