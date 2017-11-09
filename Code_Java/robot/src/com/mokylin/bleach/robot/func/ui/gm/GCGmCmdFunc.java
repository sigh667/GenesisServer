package com.mokylin.bleach.robot.func.ui.gm;

import com.mokylin.bleach.protobuf.ChatMessage.GCGmCmd;
import com.mokylin.bleach.robot.func.HumanBaseMsgFunc;
import com.mokylin.bleach.robot.gm.GMFrame;
import com.mokylin.bleach.robot.human.Human;
import com.mokylin.bleach.robot.robot.Robot;
import com.mokylin.bleach.robot.ui.PanelFactory;

public class GCGmCmdFunc extends HumanBaseMsgFunc<GCGmCmd>{

	@Override
	protected void handle(Robot robot, Human human, GCGmCmd msg) {
		GMFrame frame = PanelFactory.getGMFrame(human);
		frame.setVisible(true);
		frame.handle(msg);
	}
}
