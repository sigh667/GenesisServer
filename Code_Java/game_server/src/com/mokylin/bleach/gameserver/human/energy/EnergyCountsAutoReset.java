package com.mokylin.bleach.gameserver.human.energy;

import java.util.List;

import org.joda.time.LocalTime;

import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.core.timeaxis.ITimeEventType;
import com.mokylin.bleach.gameserver.core.autoexecutetask.AbstractAutoExecuteTask;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HumanMessage.GCBuyEnergyCountsReset;

/**
 * 体力购买次数每日刷新任务
 * @author ChangXiao
 *
 */
public class EnergyCountsAutoReset extends AbstractAutoExecuteTask<Human> {
	private final Human human;
	
	protected EnergyCountsAutoReset(LocalTime autoExecuteTime, Human human) {
		super(autoExecuteTime, human.getTimeAxis());
		this.human = human;
	}
	
	protected EnergyCountsAutoReset(List<LocalTime> autoExecuteTimeList, Human human) {
		super(autoExecuteTimeList, human.getTimeAxis());
		this.human = human;
	}
	
	@Override
	public ITimeEventType getEventType() {
		return EnergyTimeEventType.ENERGY_RECOVER;
	}
	
	@Override
	public long getEventId() {
		return 0L;
	}

	@Override
	public long getLastExecuteTime() {
		return human.get(HumanPropId.LAST_BUY_ENERGY_COUNTS_RESET_TIME);
	}

	@Override
	public void triggerExecute(Human human) {
		execute(human);
	}

	@Override
	public void autoExecute(Human human) {
		execute(human);
	}
	
	/**
	 * 确实的执行任务
	 * @param host
	 */
	private void execute(Human human) {
		if(this.isNeedExecute()) {
			final int BUY_COUNTS = human.getInt(HumanPropId.BUY_ENERGY_COUNTS);
			long now = Globals.getTimeService().now();
			
			human.set(HumanPropId.BUY_ENERGY_COUNTS, 0);
			human.set(HumanPropId.LAST_BUY_ENERGY_COUNTS_RESET_TIME, now);
			human.setModified();
			//oldCounts==0说明玩家没有购买过体力，则不需要通知客户端重置购买次数
			if (BUY_COUNTS != 0) {
				human.sendMessage(GCBuyEnergyCountsReset.newBuilder());
			}
		}
	}

}
