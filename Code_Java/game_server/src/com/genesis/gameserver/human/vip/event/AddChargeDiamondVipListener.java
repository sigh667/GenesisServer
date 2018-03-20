package com.genesis.gameserver.human.vip.event;

import com.genesis.gameserver.currency.event.AddChargeDiamondEvent;
import com.genesis.gameserver.human.vip.VipHelper;
import com.mokylin.bleach.core.event.IEventListener;

/**
 * 处理充值钻石事件，充值钻石可提升VIP经验和等级
 * @author ChangXiao
 *
 */
public class AddChargeDiamondVipListener implements IEventListener<AddChargeDiamondEvent> {

    @Override
    public void onEventOccur(AddChargeDiamondEvent event) {
        VipHelper.addExp(event.human, event.addValue);
    }

}
