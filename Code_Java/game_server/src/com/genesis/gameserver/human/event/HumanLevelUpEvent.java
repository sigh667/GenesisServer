package com.genesis.gameserver.human.event;

import com.genesis.gameserver.human.Human;

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
