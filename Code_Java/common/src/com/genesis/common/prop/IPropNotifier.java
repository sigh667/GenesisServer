package com.genesis.common.prop;

import com.genesis.common.prop.battleprop.notify.PropsToNotify;

import java.util.ArrayList;

public interface IPropNotifier {

    /**
     * 通知客户端，属性变化
     * @param list 变化的属性
     */
    public void notifyPropChanges(ArrayList<PropsToNotify> list);
}
