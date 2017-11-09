package com.mokylin.bleach.common.prop;

/**
 * 属性类型借口
 * @author yaguang.xiao
 *
 */
public interface IProp {

	/**
	 * 获取属性类型
	 * @return
	 */
	PropType getPropType();
	
	
	//TODO 记得检查所有子类的枚举名字不能重复
}
