package com.genesis.robot.func;

import com.genesis.robot.robot.Robot;
import com.google.protobuf.GeneratedMessage;

import com.genesis.robot.core.net.RobotBaseMsgFunc;
import com.genesis.robot.human.Human;

/**
 * 专门用于Human对象已经存在时的消息处理
 * @author Joey
 *
 * @param <Msg>
 */
public abstract class HumanBaseMsgFunc<Msg extends GeneratedMessage> extends RobotBaseMsgFunc<Msg> {

    @Override
    public void handle(Robot robot, Msg msg) {
        this.handle(robot, robot.getHuman(), msg);
    }

    protected abstract void handle(Robot robot, Human human, Msg msg);
}
