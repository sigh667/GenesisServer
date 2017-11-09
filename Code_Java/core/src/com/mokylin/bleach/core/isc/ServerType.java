package com.mokylin.bleach.core.isc;

import java.util.List;

import com.mokylin.bleach.core.enums.ArrayIndexedEnum;

/**
 * 服务器类型
 * 
 * 相同服务器类型的服务器之间ServerId不允许重复
 * 不同服务器类型的服务器之间ServerId允许重复
 * 
 * @author baoliang.shen
 *
 */
public enum ServerType implements ArrayIndexedEnum<ServerType>{

	/** 网关 */
	AGENT_SERVER(0),
	/** 游戏服务器 */
	GAME_SERVER(1),
	/** 数据库同步服务器 */
	DATA_SERVER(2),
	/** 日志服务器 */
	LOG_SERVER(3),
	/** 登陆服务器*/
	LOGIN_SERVER(4),
	;

	/** 按索引顺序存放的枚举数组 */
	private static final List<ServerType> indexes = ArrayIndexedEnum.EnumUtil.toIndexes(ServerType.values());
	/** index的存在，主要是为了阅读时，一眼就看到此枚举对应的数值*/
	private final int index;

	ServerType(int index) {
		this.index = index;
	}

	public static ServerType valueOf(int index) {
		return EnumUtil.valueOf(indexes, index);
	}

	@Override
	public int getIndex() {
		return this.index;
	}
}
