package com.mokylin.bleach.common.combat;

import java.util.List;

import com.mokylin.bleach.core.enums.ArrayIndexedEnum;

/**
 * 战斗类型
 * @author baoliang.shen
 *
 */
public enum CombatType implements ArrayIndexedEnum<CombatType> {
	/**竞技场*/
	Arena,
	/**公会战*/
	GuildWar,
	/**跨服战*/
	CrossWar,
	/**单人PVE*/
	SinglePve,
	;

	/** 按索引顺序存放的枚举数组 */
	private static final List<CombatType> indexes = ArrayIndexedEnum.EnumUtil.toIndexes(CombatType.values());

	@Override
	public int getIndex() {
		return this.ordinal();
	}

	public static CombatType valueOf(int index) {
		return EnumUtil.valueOf(indexes, index);
	}
}
