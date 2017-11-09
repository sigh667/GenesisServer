package com.mokylin.bleach.common.human;

import com.mokylin.bleach.core.util.arr.EnumArray;

public class HumanPropContainer {

	protected EnumArray<HumanPropId, Long> humanProp = EnumArray.create(HumanPropId.class, Long.class, 0L);

	/**
	 * 获取属性值
	 * @param humanPropId
	 * @return
	 */
	public long get(HumanPropId humanPropId) {
		return this.humanProp.get(humanPropId);
	}
	/**
	 * 设置属性值
	 * @param humanPropId
	 * @param value
	 */
	public void set(HumanPropId humanPropId, long value) {
		humanPropId.scope.checkValid(value);

		this.humanProp.set(humanPropId, value);
	}

	/**
	 * 取所有的Long型属性，以数组的形式
	 * @return
	 */
	public Iterable<? extends Long> values() {
		return humanProp.values();
	}
}
