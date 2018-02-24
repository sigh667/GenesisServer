package com.mokylin.bleach.core.isc;

import com.mokylin.bleach.core.enums.ArrayIndexedEnum;

import java.util.List;

/**
 * 服务器类型
 *
 * 相同服务器类型的服务器之间ServerId不允许重复
 * 不同服务器类型的服务器之间ServerId允许重复
 *
 * @author baoliang.shen
 *
 */
public enum ServerType implements ArrayIndexedEnum<ServerType> {

    /** 网关 */
    GATE(0),
    /** 游戏服务器 */
    GAME(1),
    /** 数据库同步服务器 */
    DB(2),
    /** 日志服务器 */
    LOG(3),
    /** 登陆服务器*/
    LOGIN(4),
    ;

    /** 按索引顺序存放的枚举数组 */
    private static final List<ServerType> indexes =
            ArrayIndexedEnum.EnumUtil.toIndexes(ServerType.values());
    /** index的存在，主要是为了阅读时，一眼就看到此枚举对应的数值*/
    private final int index;

    ServerType(int index) {
        if (index!=this.ordinal()) {
            throw new RuntimeException("Enum ServerType index must equals with ordinal! index==" + index);
        }

        this.index = index;
    }

    public static ServerType valueOf(int index) {
        return EnumUtil.valueOf(indexes, index);
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    /**
     * @return 该Server在中心Redis中的key
     */
    public String getKey() {
        return this.name();
    }
}
