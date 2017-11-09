package com.mokylin.bleach.robot.core.net;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.core.isc.session.ISession;
import com.mokylin.bleach.robot.core.msgfunc.IGCMsgFunc;
import com.mokylin.bleach.robot.robot.Robot;

public abstract class RobotBaseMsgFunc<Msg extends GeneratedMessage> implements IGCMsgFunc<Msg>{

	@Override
	public void handle(ISession session, Msg msg) {
		this.handle((Robot)session, msg);
	}
	
	public abstract void handle(Robot robot, Msg msg);
}
