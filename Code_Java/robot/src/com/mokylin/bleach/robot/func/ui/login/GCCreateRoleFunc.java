package com.mokylin.bleach.robot.func.ui.login;

import com.mokylin.bleach.protobuf.PlayerMessage.GCCreateRole;
import com.mokylin.bleach.robot.core.net.RobotBaseMsgFunc;
import com.mokylin.bleach.robot.login.Status;
import com.mokylin.bleach.robot.login.view.CreateRoleFrame;
import com.mokylin.bleach.robot.robot.Robot;
import com.mokylin.bleach.robot.ui.PanelFactory;

public class GCCreateRoleFunc extends RobotBaseMsgFunc<GCCreateRole> {

	@Override
	public void handle(Robot robot, GCCreateRole msg) {
		//1.0设置状态
		robot.setStatus(Status.CreatingRole);
		//2.0
		PanelFactory.getLoginFram().dispose();
		PanelFactory.getLoginingDialog().dispose();
		
		final String name = msg.getName();
		CreateRoleFrame createRoleFram = new CreateRoleFrame(robot,name);
		createRoleFram.setVisible(true);
		PanelFactory.setCreateRoleFram(createRoleFram);
	}

}
