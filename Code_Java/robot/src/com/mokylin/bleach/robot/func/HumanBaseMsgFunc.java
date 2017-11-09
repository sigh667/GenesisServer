package com.mokylin.bleach.robot.func;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.robot.core.net.RobotBaseMsgFunc;
import com.mokylin.bleach.robot.human.Human;
import com.mokylin.bleach.robot.robot.Robot;

/**
 * 专门用于Human对象已经存在时的消息处理
 * @author baoliang.shen
 *
 * @param <Msg>
 */
public abstract class HumanBaseMsgFunc<Msg extends GeneratedMessage> extends RobotBaseMsgFunc<Msg>{

	@Override
	public void handle(Robot robot, Msg msg) {
		this.handle(robot, robot.getHuman(), msg);
	}

	protected abstract void handle(Robot robot, Human human, Msg msg);
}
