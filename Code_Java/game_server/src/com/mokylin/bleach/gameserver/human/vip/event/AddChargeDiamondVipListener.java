package com.mokylin.bleach.gameserver.human.vip.event;

import com.mokylin.bleach.core.event.IEventListener;
import com.mokylin.bleach.gameserver.currency.event.AddChargeDiamondEvent;
import com.mokylin.bleach.gameserver.human.vip.VipHelper;

/**
 * 处理充值钻石事件，充值钻石可提升VIP经验和等级
 * @author ChangXiao
 *
 */
public class AddChargeDiamondVipListener implements IEventListener<AddChargeDiamondEvent>{

	@Override
	public void onEventOccur(AddChargeDiamondEvent event) {
		VipHelper.addExp(event.human, event.addValue);
	}
	
}
