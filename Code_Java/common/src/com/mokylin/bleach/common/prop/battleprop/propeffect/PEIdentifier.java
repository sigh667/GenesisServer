package com.mokylin.bleach.common.prop.battleprop.propeffect;

import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;

/**
 * 属性作用标识
 * @author yaguang.xiao
 *
 */
public class PEIdentifier {

	private final HeroBattlePropId id;
	private final PropEffectType effectType;

	/**
	 * 不能再本包外面创建本对象
	 * @param id
	 * @param effectType
	 */
	PEIdentifier(HeroBattlePropId id, PropEffectType effectType) {
		this.id = id;
		this.effectType = effectType;
	}

	public HeroBattlePropId getId() {
		return id;
	}

	public PropEffectType getEffectType() {
		return effectType;
	}
}
