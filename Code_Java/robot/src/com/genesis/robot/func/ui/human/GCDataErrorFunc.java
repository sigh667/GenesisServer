package com.genesis.robot.func.ui.human;

import com.genesis.protobuf.HumanMessage.GCDataError;
import com.genesis.robot.robot.Robot;
import com.genesis.robot.core.net.RobotBaseMsgFunc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
