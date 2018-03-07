package com.mokylin.bleach.core.isc;

import com.genesis.protobuf.MessageType;
import com.mokylin.bleach.core.enums.ArrayIndexedEnum;

import java.util.List;

/**
 * 服务器类型
 *
 * 相同服务器类型的服务器之间ServerId不允许重复
 * 不同服务器类型的服务器之间ServerId允许重复
 *
 * @author Joey
 *
 */
public enum ServerType implements ArrayIndexedEnum<ServerType> {
    /** 登陆服务器*/
    LOGIN(0, MessageType.MessageTarget.LOGIN_SERVER),
    /** 网关 */
    GATE(1, MessageType.MessageTarget.GATE_SERVER),
    /** 游戏服务器 */
    GAME(2, MessageType.MessageTarget.GAME_SERVER),
    /** 数据库同步服务器 */
    DB(3, null),
    /** 数据库同步服务器(登陆专用) */
    DB_LOGIN(4, null),
    /** 日志服务器 */
    LOG(5, null),
    /** GM后台服务器 */
    DIP(6, null),
    /** 匹配服务器 */
    MATCH(7, MessageType.MessageTarget.MATCH_SERVER),
    /** 战斗服务器 */
    FIGHT(8, MessageType.MessageTarget.FIGHT_SERVER),
    ;

    /** 按索引顺序存放的枚举数组 */
    private static final List<ServerType> indexes = ArrayIndexedEnum.EnumUtil.toIndexes(ServerType.values());
    public static ServerType valueOf(int index) {
        return EnumUtil.valueOf(indexes, index);
    }


    /** index的存在，主要是为了阅读时，一眼就看到此枚举对应的数值*/
    private final int index;
    /** 映射的Target */
    public final MessageType.MessageTarget target;

    ServerType(int index, MessageType.MessageTarget target) {
        if (index!=this.ordinal()) {
            throw new RuntimeException("Enum ServerType index must equals with ordinal! index==" + index);
        }

        this.index = index;
        this.target = target;
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
