package com.mokylin.bleach.common.human;

import java.util.Map;

import com.mokylin.bleach.common.prop.IProp;
import com.mokylin.bleach.common.prop.PropScope;
import com.mokylin.bleach.common.prop.PropType;

public enum HumanPropId implements IProp {
	/** 队伍可成长属性 */
	/**等级*/
	LEVEL(0, Long.MAX_VALUE),
	/**当前经验值*/
	EXP(0, Long.MAX_VALUE),
	/**当前体力*/
	ENERGY(0, Long.MAX_VALUE),
	/**上次恢复体力时间*/
	LAST_ENERGY_RECOVER_TIME(0, Long.MAX_VALUE),
	/**当日已购买体力次数*/
	BUY_ENERGY_COUNTS(0, Long.MAX_VALUE),
	/**上次体力购买次数重置时间*/
	LAST_BUY_ENERGY_COUNTS_RESET_TIME(0, Long.MAX_VALUE),
	/**VIP等级*/
	VIP_LEVEL(0, Long.MAX_VALUE),
	/**VIP经验*/
	VIP_EXP(0, Long.MAX_VALUE),
	/**当前技能点数*/
	SKILL_POINT(0, Long.MAX_VALUE),
	/**今日已经购买技能点几次了*/
	BUY_SKILL_POINT_TIME(0, Long.MAX_VALUE),
	;

	private static Map<String, HumanPropId> reflect = PropType
			.constructReflect(HumanPropId.class,
					PropType.HUMAN);

	/**
	 * 获取数字对应的枚举
	 * 
	 * @param idName
	 * @return
	 */
	public static HumanPropId get(int idName) {
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

	HumanPropId(long minValue, long maxValue) {
		this.scope = new PropScope(minValue, maxValue);
	}

	@Override
	public PropType getPropType() {
		return PropType.HUMAN;
	}

}
