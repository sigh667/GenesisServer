package com.mokylin.bleach.common.prop.battleprop;

import com.mokylin.bleach.common.prop.battleprop.propholder.FinalProp;

/**
 * 属性容器
 * 
 * <p>
 * 此接口的作用是隔离属性管理器中{@link FinalProp}不需要的方法
 * @author yaguang.xiao
 *
 */
public interface IPropContainer extends IPropObserver {

	/**
	 * 初始化时重新计算属性，此方法不会向客户端发送属性更新消息
	 */
	void calculate();
	
	/**
	 * 重新计算属性，此方法会向客户端发送属性更新消息
	 */
	void calculateAndNotify();
}
