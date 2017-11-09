package com.mokylin.bleach.common.combat.enumeration;

/**
 * 招式伤害类型
 * @author baoliang.shen
 *
 */
public enum SpellDamageType {
	DoNothing(0),			//Do Nothing
	NormalAttack(1),  		//普通攻击
	SpellAttack(1 << 2),	//刀技
	ZhuanJingAttack(1 << 3),//专精技能,  鬼道.e.g
	;
	
	private int index;
	SpellDamageType(int index){
		this.index = index;
	}
	public int getIndex() {
		return index;
	}

}
