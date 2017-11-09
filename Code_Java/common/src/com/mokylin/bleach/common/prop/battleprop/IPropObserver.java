package com.mokylin.bleach.common.prop.battleprop;

import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;

/**
 * 属性观察者
 * @author yaguang.xiao
 *
 */
public interface IPropObserver {

	/**
	 * 更新属性
	 * @param changes	属性改变效果
	 */
	void update(BattlePropEffect... changes);
}
