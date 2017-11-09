package com.mokylin.bleach.robot.core.event;

import com.mokylin.bleach.robot.robot.Robot;

public abstract class RobotEvent {
	
	private final Robot robot;

	public RobotEvent(Robot robot) {
		this.robot = robot;
	}

	public Robot getRobot() {
		return robot;
	}
}
