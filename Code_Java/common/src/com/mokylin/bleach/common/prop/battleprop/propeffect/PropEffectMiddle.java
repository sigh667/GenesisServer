package com.mokylin.bleach.common.prop.battleprop.propeffect;

public class PropEffectMiddle {

	private final int propId;
	private final PropEffectType effectType;
	private final int value;

	public PropEffectMiddle(int propId, PropEffectType effectType, int value) {
		this.propId = propId;
		this.effectType = effectType;
		this.value = value;
	}

	public int getPropId() {
		return propId;
	}

	public PropEffectType getEffectType() {
		return effectType;
	}

	public int getValue() {
		return value;
	}

}
