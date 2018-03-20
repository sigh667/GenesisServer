package com.genesis.common.combat.enumeration;

/**
 * Buff命中规则
 * @author Joey
 *
 */
public enum SpellBuffHitType {

    HitNoMiss,            //命中率为100%，必中
    HitWhileSpellHit,    //当技能命中时，buff生效
    ;
}
