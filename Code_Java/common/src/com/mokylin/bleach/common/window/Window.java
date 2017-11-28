package com.mokylin.bleach.common.window;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 窗口枚举
 * @author yaguang.xiao
 *
 */
public enum Window {

    /** 普通商店 */
    GENERAL_SHOP(0), /** 神秘商店 */
    MYSTERIOUS_SHOP(1),;

    private static Map<Integer, Window> values = Maps.newHashMap();

    static {
        for (Window window : Window.values()) {
            values.put(window.getIndex(), window);
        }
    }

    private final int index;

    Window(int index) {
        this.index = index;
    }

    /**
     * 根据数字标识获取对应的窗口枚举
     * @param index
     * @return
     */
    public static Window getByIndex(int index) {
        return values.get(index);
    }

    /**
     * 判断指定的数字标识是否有效
     * @param index
     * @return
     */
    public static boolean isValid(int index) {
        return values.containsKey(index);
    }

    public int getIndex() {
        return this.index;
    }
}
