package com.mokylin.bleach.common.prop;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 所有属性类型
 * @author yaguang.xiao
 *
 */
public enum PropType {

	/** 钱 */
	CURRENCY,
	/** 队伍属性 */
	HUMAN,

	/** 英雄属性 */
	HERO,
	/** 英雄战斗属性 */
	HERO_BATTLE,
	;
	
	private static HashMap<String, PropType> name2TypeMap = new HashMap<>();
	
	private static HashMap<String, IProp> name2IPropMap = new HashMap<>();
	
	/**
	 * 构建属性数字Id与属性枚举的对应关系
	 * @param propClass 属性枚举类
	 * @param propType	属性类型
	 * @return	<属性数字Id, 属性枚举>
	 */
	public static <T extends IProp> Map<String, T> constructReflect(Class<T> propClass, PropType propType) {
		Map<String, T> reflect = Maps.newHashMap();
		if(propClass == null || propType == null) {
			return reflect;
		}
		
		for(T prop : propClass.getEnumConstants()) {
			String propName = prop.toString();	
			reflect.put(propName, prop);
			
			name2TypeMap.put(propName, propType);
			name2IPropMap.put(propName, prop);
		}
		
		return reflect;
	}
	
	/**
	 * 获取属性的类型
	 * @param id
	 * @return
	 */
	public static PropType getType(String name) {
		return name2TypeMap.get(name);
	}

	/**
	 * 获取具体属性枚举对象
	 * @param name
	 * @return
	 */
	public static IProp getIProp(String name) {
		return name2IPropMap.get(name);
	}
}
