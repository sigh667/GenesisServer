package com.mokylin.bleach.robot.robot;

import com.icewind.protobuf.HumanMessage.GCHumanDetailInfo;
import com.mokylin.bleach.robot.human.Human;

import io.netty.channel.ChannelHandlerContext;

public class Robot extends RobotSession {

    private Human human = null;

    public Robot(ChannelHandlerContext ctx) {
        super(ctx);
    }

    public void initHuman(GCHumanDetailInfo msg) {
        human = new Human(this);
        human.init(msg);
    }

    public Human getHuman() {
        return this.human;
    }

}
