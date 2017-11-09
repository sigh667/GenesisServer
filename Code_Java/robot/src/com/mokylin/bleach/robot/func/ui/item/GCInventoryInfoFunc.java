package com.mokylin.bleach.robot.func.ui.item;

import com.mokylin.bleach.protobuf.ItemMessage.GCInventoryInfo;
import com.mokylin.bleach.robot.func.HumanBaseMsgFunc;
import com.mokylin.bleach.robot.human.Human;
import com.mokylin.bleach.robot.robot.Robot;

public class GCInventoryInfoFunc extends HumanBaseMsgFunc<GCInventoryInfo> {

	@Override
	protected void handle(Robot robot, Human human, GCInventoryInfo msg) {
		human.getInventory().load(msg);
	}
}
