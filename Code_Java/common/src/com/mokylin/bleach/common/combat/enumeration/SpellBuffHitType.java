package com.mokylin.bleach.common.combat.enumeration;

/**
 * Buff命中规则
 * @author baoliang.shen
 *
 */
public enum SpellBuffHitType {

	HitNoMiss,			//命中率为100%，必中
	HitWhileSpellHit,	//当技能命中时，buff生效
	;
}
