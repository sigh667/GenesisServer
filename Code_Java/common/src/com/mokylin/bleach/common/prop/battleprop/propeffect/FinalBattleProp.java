package com.mokylin.bleach.common.prop.battleprop.propeffect;

import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;

/**
 * 属性作用
 * 
 * @author yaguang.xiao
 * 
 */
public class FinalBattleProp {

	/** 属性作用标识 */
	private final PEIdentifier pEId;
	/** 属性作用的值(为了避免累计误差，所以策划表格中必须要填整数:属性计算式以增量的方式算的) */
	private int value;

	public FinalBattleProp(HeroBattlePropId propId, PropEffectType type, int value) {
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
	 * 改变数值
	 * @param changeV
	 */
	public void changeValue(int changeV) {
		this.value += changeV;
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

}
