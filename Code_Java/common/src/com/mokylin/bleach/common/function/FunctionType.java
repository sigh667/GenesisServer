package com.mokylin.bleach.common.function;

import java.util.List;

import com.mokylin.bleach.core.enums.ArrayIndexedEnum;

/**
 * 功能枚举
 * @author yaguang.xiao
 *
 */
public enum FunctionType implements ArrayIndexedEnum<FunctionType> {

	GENERAL_SHOP,
	MYSTERIOUS_SHOP,
	;
	
	/** 按索引顺序存放的枚举数组 */
	private static final List<FunctionType> indexes = ArrayIndexedEnum.EnumUtil.toIndexes(FunctionType.values());

	public static FunctionType valueOf(int index) {
		return ArrayIndexedEnum.EnumUtil.valueOf(indexes, index);
	}

	@Override
	public int getIndex() {
		return this.ordinal();
	}
}
