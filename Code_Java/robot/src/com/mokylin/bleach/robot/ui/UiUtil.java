package com.mokylin.bleach.robot.ui;

import javax.swing.*;

public class UiUtil {

    public static void switchShowOrHide(JFrame frame) {
        if (frame.isVisible()) {
            frame.setVisible(false);
        } else {
            frame.setVisible(true);
        }
    }

}
