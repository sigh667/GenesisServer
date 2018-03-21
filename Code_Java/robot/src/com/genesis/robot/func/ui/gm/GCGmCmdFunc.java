package com.genesis.robot.func.ui.gm;

import com.genesis.protobuf.ChatMessage.GCGmCmd;
import com.genesis.robot.func.HumanBaseMsgFunc;
import com.genesis.robot.gm.GMFrame;
import com.genesis.robot.human.Human;
import com.genesis.robot.robot.Robot;
import com.genesis.robot.ui.PanelFactory;

public class GCGmCmdFunc extends HumanBaseMsgFunc<GCGmCmd> {

    @Override
    protected void handle(Robot robot, Human human, GCGmCmd msg) {
        GMFrame frame = PanelFactory.getGMFrame(human);
        frame.setVisible(true);
        frame.handle(msg);
    }
}
