package com.genesis.robot.func.ui.item;

import com.genesis.protobuf.ItemMessage.GCInventoryInfo;
import com.genesis.robot.func.HumanBaseMsgFunc;
import com.genesis.robot.robot.Robot;
import com.genesis.robot.human.Human;

public class GCInventoryInfoFunc extends HumanBaseMsgFunc<GCInventoryInfo> {

    @Override
    protected void handle(Robot robot, Human human, GCInventoryInfo msg) {
        human.getInventory().load(msg);
    }
}
