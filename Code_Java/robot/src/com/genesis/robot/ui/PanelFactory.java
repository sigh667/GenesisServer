package com.genesis.robot.ui;

import com.genesis.robot.gm.GMFrame;
import com.genesis.robot.human.Human;
import com.genesis.robot.item.Inventory;
import com.genesis.robot.item.view.InventoryFrame;
//import com.mokylin.bleach.robot.login.view.CreateRoleFrame;
//import com.mokylin.bleach.robot.login.view.SelectRoleFrame;
import com.genesis.robot.ui.loading.LoadingDialog;

import javax.swing.*;

public class PanelFactory {

    private static JFrame loginFram;

    private static LoadingDialog loginingDialog;

//    private static CreateRoleFrame createRoleFram;
//
//    private static SelectRoleFrame selectRoleFrame;

    private static InventoryFrame inventoryFrame;

    private static GMFrame gmFrame;


    public static LoadingDialog getLoginingDialog() {
        if (loginingDialog == null) {
            loginingDialog = new LoadingDialog();
        }
        return loginingDialog;
    }

    public static JFrame getLoginFram() {
        return loginFram;
    }

    public static void setLoginFram(JFrame loginFram) {
        PanelFactory.loginFram = loginFram;
    }

//    public static CreateRoleFrame getCreateRoleFram() {
//        return createRoleFram;
//    }
//
//    public static void setCreateRoleFram(CreateRoleFrame createRoleFram) {
//        PanelFactory.createRoleFram = createRoleFram;
//    }

    public static InventoryFrame getInventoryFrame(Inventory inventory) {
        if (inventoryFrame == null) {
            inventoryFrame = new InventoryFrame(inventory);
        }

        return inventoryFrame;
    }

    public static GMFrame getGMFrame(Human human) {
        if (gmFrame == null) {
            gmFrame = new GMFrame(human);
        }

        return gmFrame;
    }

//    public static SelectRoleFrame getSelectRoleFrame() {
//        return selectRoleFrame;
//    }
//
//    public static void setSelectRoleFrame(SelectRoleFrame frame) {
//        selectRoleFrame = frame;
//    }
}
