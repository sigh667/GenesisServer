package com.mokylin.bleach.gameserver.human.energy;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.timeaxis.ITimeEvent;
import com.mokylin.bleach.core.timeaxis.ITimeEventType;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HumanMessage.GCEnergyRecoverInfo;

/**
 * 体力自动恢复任务
 *
 */
public class EnergyRecoverTask implements ITimeEvent<Human> {

	@Override
	public ITimeEventType getEventType() {
		return EnergyTimeEventType.ENERGY_RECOVER;
	}

	@Override
	public long getEventId() {
		return 0L;
	}

	@Override
	public void eventOccur(Human human) {
		//恢复体力值，默认为1
		int recoverValue = GlobalData.getConstants().getEnergyRecoverValue();
		//恢复值 > 0则通知客户端
		int added = human.getEnergyManager().recoverEnergy(recoverValue);
		if (added > 0) {
			human.sendMessage(GCEnergyRecoverInfo.newBuilder().setValue(added));
		}
	}

}
