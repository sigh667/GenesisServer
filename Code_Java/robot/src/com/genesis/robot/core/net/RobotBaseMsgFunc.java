package com.genesis.robot.core.net;

import com.genesis.robot.core.msgfunc.IGCMsgFunc;
import com.genesis.robot.robot.Robot;
import com.google.protobuf.GeneratedMessage;

import com.genesis.servermsg.core.isc.session.ISession;

public abstract class RobotBaseMsgFunc<Msg extends GeneratedMessage> implements IGCMsgFunc<Msg> {

    @Override
    public void handle(ISession session, Msg msg) {
        this.handle((Robot) session, msg);
    }

    public abstract void handle(Robot robot, Msg msg);
}
