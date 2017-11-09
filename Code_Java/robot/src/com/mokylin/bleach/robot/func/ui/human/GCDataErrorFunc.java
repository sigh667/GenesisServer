package com.mokylin.bleach.robot.func.ui.human;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.protobuf.HumanMessage.GCDataError;
import com.mokylin.bleach.robot.core.net.RobotBaseMsgFunc;
import com.mokylin.bleach.robot.robot.Robot;

public class GCDataErrorFunc extends RobotBaseMsgFunc<GCDataError> {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(GCDataErrorFunc.class);

	@Override
	public void handle(Robot robot, GCDataError msg) {
		//1.0数据错误，服务器要把我踢了，故而发来此消息
		logger.warn("服务器将Robot【AccountId={}】踢下线", robot.getId());

		// TODO Auto-generated method stub
	}

}
