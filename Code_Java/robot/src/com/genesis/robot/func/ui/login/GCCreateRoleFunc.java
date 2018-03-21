package com.genesis.robot.func.ui.login;
//
//import com.genesis.protobuf.PlayerMessage.GCCreateRole;
//import RobotBaseMsgFunc;
//import Status;
//import com.mokylin.bleach.robot.login.view.CreateRoleFrame;
//import Robot;
//import PanelFactory;
//
//public class GCCreateRoleFunc extends RobotBaseMsgFunc<GCCreateRole> {
//
//    @Override
//    public void handle(Robot robot, GCCreateRole msg) {
//        //1.0设置状态
//        robot.setStatus(Status.CreatingRole);
//        //2.0
//        PanelFactory.getLoginFram().dispose();
//        PanelFactory.getLoginingDialog().dispose();
//
//        final String name = msg.getName();
//        CreateRoleFrame createRoleFram = new CreateRoleFrame(robot, name);
//        createRoleFram.setVisible(true);
//        PanelFactory.setCreateRoleFram(createRoleFram);
//    }
//
//}
