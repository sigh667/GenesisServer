package com.mokylin.bleach.common.prop;

import java.util.ArrayList;

import com.mokylin.bleach.common.prop.battleprop.notify.PropsToNotify;

public interface IPropNotifier {

	/**
	 * 通知客户端，属性变化
	 * @param list 变化的属性
	 */
	public void notifyPropChanges(ArrayList<PropsToNotify> list);
}
