package com.mokylin.bleach.gamedb.redis.key;


/**
 * 枚举Redis中所有key的类型
 * <br>采用首字母大写，而不使用“_”连接单词，因为这样能节省一个字符，减少key长度</br>
 * @author baoliang.shen
 *
 */
public enum RedisKeyType {

	/** 服务器状态 */
	ServerStatus,
	/**角色表*/
	Human,
	/**道具表*/
	Item,
	/**竞技场镜像*/
	ArenaSnap,
	/**英雄*/
	Hero,
	/** 商店 */
	Shop,
	/** 物品打折 */
	ShopDiscount,
	/** 功能 */
	Function,
//	/**公会表*/
//	Guild,
//	/**公会成员表*/
//	GuildMember,
	;

}
