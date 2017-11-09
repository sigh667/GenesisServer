package com.mokylin.bleach.gameserver.hero;

import java.util.Map;

import com.mokylin.bleach.common.prop.IProp;
import com.mokylin.bleach.common.prop.PropScope;
import com.mokylin.bleach.common.prop.PropType;

public enum HeroPropId implements IProp {
	/** 英雄可成长属性*/
	LEVEL(0, Long.MAX_VALUE),
	EXP(0, Long.MAX_VALUE),
	/** 英雄不可成长属性 */
	
	;

	private static Map<String, HeroPropId> reflect = PropType
			.constructReflect(HeroPropId.class,
					PropType.HERO);
	
	/**
	 * 根据属性数字Id获取对应的HeroChangeablePropId
	 * @param idName
	 * @return
	 */
	public static HeroPropId get(int idName) {
		return reflect.get(idName);
	}
	
	/**
	 * 判断指定的索引是否有被定义
	 * @param idIndex
	 * @return
	 */
	public static boolean containsIndex(int idIndex) {
		return reflect.containsKey(idIndex);
	}

	public final PropScope scope;

	HeroPropId(long minValue, long maxValue) {
		this.scope = new PropScope(minValue, maxValue);
	}

	@Override
	public PropType getPropType() {
		return PropType.HERO;
	}

}
