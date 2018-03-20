package com.genesis.gameserver.currency.event;

import com.genesis.gameserver.human.Human;

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
