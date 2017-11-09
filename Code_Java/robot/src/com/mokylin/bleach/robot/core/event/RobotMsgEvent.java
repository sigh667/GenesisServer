package com.mokylin.bleach.robot.core.event;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.robot.robot.Robot;

public abstract class RobotMsgEvent<Msg extends GeneratedMessage> extends RobotEvent{
	
	private Msg msg;

	public RobotMsgEvent(Robot robot, Msg msg) {
		super(robot);
		this.msg = msg;
	}

	public Msg getMsg() {
		return msg;
	}

}
