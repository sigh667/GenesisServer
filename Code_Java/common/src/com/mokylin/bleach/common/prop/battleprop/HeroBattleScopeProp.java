package com.mokylin.bleach.common.prop.battleprop;

/**
 * 战斗上下限属性
 * 
 * @author yaguang.xiao
 * 
 */
public enum HeroBattleScopeProp {

	/** 攻击 */
	ATTACK(HeroBattlePropId.GongJiLiMin, HeroBattlePropId.GongJiLiMax)
	;

	/** 下限ID */
	private final HeroBattlePropId lowerBoundId;
	/** 上限ID */
	private final HeroBattlePropId upperBoundId;

	HeroBattleScopeProp(HeroBattlePropId lowerBoundId, HeroBattlePropId upperBoundId) {
		this.lowerBoundId = lowerBoundId;
		this.upperBoundId = upperBoundId;
	}

	public HeroBattlePropId getLowerBoundId() {
		return lowerBoundId;
	}

	public HeroBattlePropId getUpperBoundId() {
		return upperBoundId;
	}

}
