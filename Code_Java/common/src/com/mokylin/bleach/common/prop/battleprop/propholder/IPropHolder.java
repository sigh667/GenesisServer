package com.mokylin.bleach.common.prop.battleprop.propholder;

import com.mokylin.bleach.common.prop.battleprop.BattlePropContainer;

/**
 * 属性持有者
 * @author yaguang.xiao
 *
 * @param <T>	属性分布枚举
 */
public interface IPropHolder<T extends Enum<T>> {

	/**
	 * 获取属性
	 * @return
	 */
	Prop<T> getProp();

	/**
	 * 将我携带的属性添加到battlePropContainer中
	 * @param battlePropContainer
	 */
	void addProp(BattlePropContainer battlePropContainer);
}
