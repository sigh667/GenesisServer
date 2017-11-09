package com.mokylin.bleach.robot.func.ui.login;

import com.mokylin.bleach.protobuf.HumanMessage.GCHumanDetailInfo;
import com.mokylin.bleach.robot.core.net.RobotBaseMsgFunc;
import com.mokylin.bleach.robot.login.Status;
import com.mokylin.bleach.robot.login.view.CreateRoleFrame;
import com.mokylin.bleach.robot.login.view.SelectRoleFrame;
import com.mokylin.bleach.robot.main.MainFrame;
import com.mokylin.bleach.robot.robot.Robot;
import com.mokylin.bleach.robot.ui.PanelFactory;

public class GCHumanDetailInfoFunc extends RobotBaseMsgFunc<GCHumanDetailInfo> {

	@Override
	public void handle(Robot robot, GCHumanDetailInfo msg) {
		//1.0
		Status status = robot.getStatus();
		if (status==Status.CreatingRole) {
			CreateRoleFrame createRoleFram = PanelFactory.getCreateRoleFram();
			if (createRoleFram!=null)
				createRoleFram.dispose();
		} else if (status==Status.SelectingRole) {
			SelectRoleFrame selectRoleFrame = PanelFactory.getSelectRoleFrame();
			if (selectRoleFrame!=null)
				selectRoleFrame.dispose();
		} else {
			PanelFactory.getLoginFram().dispose();
			PanelFactory.getLoginingDialog().dispose();
		}
		
		//2.0设置状态
		robot.setStatus(Status.Gaming);

		//3.0打开主界面
		MainFrame frame = new MainFrame(robot,msg);
		frame.setVisible(true);
	}

}
