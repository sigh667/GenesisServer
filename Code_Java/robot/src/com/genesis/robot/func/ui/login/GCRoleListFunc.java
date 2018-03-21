package com.genesis.robot.func.ui.login;
//
//import com.genesis.protobuf.PlayerMessage.GCRoleList;
//import com.genesis.protobuf.PlayerMessage.Role;
//import RobotBaseMsgFunc;
//import Status;
//import com.mokylin.bleach.robot.login.view.SelectRoleFrame;
//import com.mokylin.bleach.robot.login.view.model.RoleToView;
//import Robot;
//import PanelFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GCRoleListFunc extends RobotBaseMsgFunc<GCRoleList> {
//
//    @Override
//    public void handle(Robot robot, GCRoleList msg) {
//        //1.0设置状态
//        robot.setStatus(Status.SelectingRole);
//        //2.0
//        PanelFactory.getLoginFram().dispose();
//        PanelFactory.getLoginingDialog().dispose();
//
//        final List<Role> rolesList = msg.getRolesList();
//        final List<RoleToView> viewList = new ArrayList<>();
//        for (Role role : rolesList) {
//            RoleToView roleToView = new RoleToView(role);
//            viewList.add(roleToView);
//        }
//        SelectRoleFrame frame = new SelectRoleFrame(robot, viewList);
//        PanelFactory.setSelectRoleFrame(frame);
//        frame.setVisible(true);
//    }
//
//}
