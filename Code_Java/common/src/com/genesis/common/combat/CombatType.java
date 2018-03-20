package com.genesis.common.combat;

import com.genesis.core.enums.ArrayIndexedEnum;

import java.util.List;

/**
 * 战斗类型
 * @author Joey
 *
 */
public enum CombatType implements ArrayIndexedEnum<CombatType> {
    /**竞技场*/
    Arena, /**公会战*/
    GuildWar, /**跨服战*/
    CrossWar, /**单人PVE*/
    SinglePve,;

    /** 按索引顺序存放的枚举数组 */
    private static final List<CombatType> indexes =
            ArrayIndexedEnum.EnumUtil.toIndexes(CombatType.values());

    public static CombatType valueOf(int index) {
        return EnumUtil.valueOf(indexes, index);
    }

    @Override
    public int getIndex() {
        return this.ordinal();
    }
}
