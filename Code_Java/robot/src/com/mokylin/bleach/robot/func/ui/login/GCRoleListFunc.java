package com.mokylin.bleach.robot.func.ui.login;
//
//import com.icewind.protobuf.PlayerMessage.GCRoleList;
//import com.icewind.protobuf.PlayerMessage.Role;
//import com.mokylin.bleach.robot.core.net.RobotBaseMsgFunc;
//import com.mokylin.bleach.robot.login.Status;
//import com.mokylin.bleach.robot.login.view.SelectRoleFrame;
//import com.mokylin.bleach.robot.login.view.model.RoleToView;
//import com.mokylin.bleach.robot.robot.Robot;
//import com.mokylin.bleach.robot.ui.PanelFactory;
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
