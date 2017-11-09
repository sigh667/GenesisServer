package com.mokylin.bleach.common.prop.battleprop.notify;

public class PropsToNotify {

	private int propId;
	private float value;

	public PropsToNotify(int propId, float newValue) {
		this.propId = propId;
		this.value = newValue;
	}
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
}
