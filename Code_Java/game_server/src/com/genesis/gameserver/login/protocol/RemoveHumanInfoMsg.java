package com.genesis.gameserver.login.protocol;

import com.genesis.gamedb.human.HumanInfo;

public class RemoveHumanInfoMsg {

    private HumanInfo humanInfo;

    public RemoveHumanInfoMsg(HumanInfo humanInfo) {
        this.humanInfo = humanInfo;
    }

    public HumanInfo getHumanInfo() {
        return humanInfo;
    }
}
