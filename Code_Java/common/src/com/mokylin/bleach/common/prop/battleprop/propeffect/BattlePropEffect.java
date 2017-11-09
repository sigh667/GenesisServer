package com.mokylin.bleach.common.prop.battleprop.propeffect;

import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;

/**
 * 属性作用
 * 
 * @author yaguang.xiao
 * 
 */
public class BattlePropEffect {

	/** 属性作用标识 */
	private final PEIdentifier pEId;
	/** 属性作用的值(为了避免累计误差，所以策划表格中必须要填整数:属性计算式以增量的方式算的) */
	private final int value;

	public BattlePropEffect(HeroBattlePropId propId, PropEffectType type, int value) {
		this.pEId = PEIdFactory.get(propId, type);
		this.value = value;
	}
	
	/**
	 * 获取属性作用标识
	 * @return
	 */
	public PEIdentifier getPEId() {
		return this.pEId;
	}

	/**
	 * 获取属性标识
	 * @return
	 */
	public HeroBattlePropId getPropId() {
		return this.pEId.getId();
	}

	/**
	 * 获取属性作用类型
	 * @return
	 */
	public PropEffectType getType() {
		return this.pEId.getEffectType();
	}

	/**
	 * 获取属性作用的值
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 是不是相同的类型(相同的属性类型和相同的影响类型)
	 * @param propId
	 * @param type
	 * @return
	 */
	public boolean isSameType(HeroBattlePropId propId, PropEffectType type) {
		if(this.pEId.getId() == propId && this.pEId.getEffectType() == type) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 是不是相同的类型(相同的属性类型和相同的影响类型)
	 * @param prop
	 * @return
	 */
	public boolean isSameType(FinalBattleProp prop) {
		if(prop == null || !prop.isValid()) {
			return false;
		}
		
		if(this.pEId.getId() == prop.getPropId() && this.pEId.getEffectType() == prop.getType()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 是否有效
	 * @return
	 */
	public boolean isValid() {
		if(this.pEId == null || value == 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 克隆一个FinalBattleProp对象
	 * @return
	 */
	public FinalBattleProp cloneFinalBattleProp() {
		return new FinalBattleProp(this.getPropId(), this.getType(), this.value);
	}

}
