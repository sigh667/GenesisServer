package com.mokylin.bleach.core.uuid;

import com.mokylin.bleach.core.annotation.Immutable;

/**
 * 64位Id生成器模板
 * 
 * <p>所有类型UUID的公共模板
 * <p>这里保存定义好UUID组合策略之后不会再改变的信息
 * 
 * @author yaguang.xiao
 * 
 */

@Immutable
class UUID64Template {
	
	/** 可使用的最大位数 */
	private static final int MAX_BIT_NUM = 63;

	/** 服务器组Id */
	private final long serverGroup;
	/** 服务器Id */
	private final long serverId;
	
	/** UUID最小值 */
	private final long minUUID;
	/** UUID最大值 */
	private final long maxUUID;
	
	/**
	 * 构造UUID模板
	 * @param serverGroupBitNum	服务器组id位数
	 * @param serverBitNum	服务器id位数
	 * @param objectBitNum	对象id位数
	 * @param serverGroup		渠道id
	 * @param serverId		服务器Id
	 */
	UUID64Template(int serverGroupBitNum, int serverBitNum, int objectBitNum, int serverGroup, int serverId) {
		// 检查参数合法性
		if (serverGroupBitNum < 1 || serverBitNum < 1 || objectBitNum < 1)
			throw new IllegalArgumentException(
					"The channelBitNum, serverBitNum, objectBitNum must be >= 1");
		if (serverGroupBitNum + serverBitNum + objectBitNum > MAX_BIT_NUM)
			throw new IllegalArgumentException("the sum bit num must be <= 63");
		if (serverGroup < 0 || serverId < 0)
			throw new IllegalArgumentException(
					"All the channelId, serverId must be >= 0");
		
		this.serverGroup = serverGroup;
		this.serverId = serverId;
		
		long high = this.serverGroup << (serverBitNum + objectBitNum); // 得到渠道Id位
		this.minUUID = high | ((serverId) << objectBitNum);
		long objectIdMask = ((1L) << objectBitNum) - 1;// 得到对象Id的掩码
		this.maxUUID = this.minUUID | objectIdMask;
	}
	
	UUID64Template(int serverGroupBitNum, int serverBitNum, int objectBitNum, long uuid) {
		// 检查参数合法性
		if (serverGroupBitNum < 1 || serverBitNum < 1 || objectBitNum < 1)
			throw new IllegalArgumentException(
					"The channelBitNum, serverBitNum, objectBitNum must be >= 1");
		if (serverGroupBitNum + serverBitNum + objectBitNum > MAX_BIT_NUM)
			throw new IllegalArgumentException("the sum bit num must be <= 63");
		if(uuid < 0) {
			throw new IllegalArgumentException("uuid must be >= 0");
		}
		
		this.serverGroup = uuid >> (serverBitNum + objectBitNum);
		this.serverId = (int) ((uuid << serverGroupBitNum) >> (serverGroupBitNum + objectBitNum));
		
		long high = this.serverGroup << (serverBitNum + objectBitNum); // 得到渠道Id位
		this.minUUID = high | ((serverId) << objectBitNum);
		long objectIdMask = ((1L) << objectBitNum) - 1;// 得到对象Id的掩码
		this.maxUUID = this.minUUID | objectIdMask;
	}
	
	/**
	 * 获取最小UUID
	 * @return
	 */
	public long getMinUUID() {
		return minUUID;
	}
	
	/**
	 * 获取最大UUID
	 * @return
	 */
	public long getMaxUUID() {
		return this.maxUUID;
	}

	/**
	 * 检查指定的位数能否表示指定的值
	 * 
	 * @param bitNum
	 * @param value
	 */
	public static void checkBitsMaxNum(int bitNum, long value) {
		long maxValue = (((long)1) << bitNum) - 1;
		if (value > maxValue || value < 0)
			throw new IllegalArgumentException("Can't represent value[" + value
					+ "] with " + bitNum + "bits!!");
	}
}
