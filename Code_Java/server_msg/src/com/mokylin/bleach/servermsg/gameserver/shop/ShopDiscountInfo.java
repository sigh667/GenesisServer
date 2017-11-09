package com.mokylin.bleach.servermsg.gameserver.shop;

import java.sql.Timestamp;

/**
 * 商店打折信息消息
 * @author Administrator
 *
 */
public class ShopDiscountInfo {
	/** 服务器Id */
	public final int serverId;
	/** 商店类型 */
	public final int shopTypeId;
	/** 折扣 */
	public final int discount;
	/** 数量倍数 */
	public final int numMultiple;
	/** 折扣开始时间 */
	public final Timestamp startTime;
	/** 折扣结束时间 */
	public final Timestamp endTime;
	
	public ShopDiscountInfo(int serverId, int shopTypeId, int discount, int numMultiple, Timestamp startTime, Timestamp endTime) {
		this.serverId = serverId;
		this.shopTypeId = shopTypeId;
		this.discount = discount;
		this.numMultiple = numMultiple;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
