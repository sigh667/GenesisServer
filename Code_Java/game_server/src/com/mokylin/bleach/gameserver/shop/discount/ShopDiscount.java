package com.mokylin.bleach.gameserver.shop.discount;

import java.sql.Timestamp;

import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.gamedb.orm.entity.ShopDiscountEntity;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.persistance.ObjectInSqlImplNoCache;

/**
 * 物品折扣信息
 * 
 * @author yaguang.xiao
 * 
 */
public class ShopDiscount extends ObjectInSqlImplNoCache<Long, ShopDiscountEntity> {

	/** Id */
	private long id;
	/** 服务器Id */
	private int serverId;
	/** 商店类型 */
	private ShopType shopType;
	/** 折扣 */
	private int discount;
	/** 数量倍数 */
	private int numMultiple;
	/** 折扣开始时间 */
	private long startTime;
	/** 折扣结束时间 */
	private long endTime;

	/**
	 * 创建打折的时候调用
	 * @param sGlobals
	 * @param id
	 * @param serverId
	 * @param shopType
	 * @param discount
	 * @param numMultiple
	 * @param startTime
	 * @param endTime
	 */
	public ShopDiscount(ServerGlobals sGlobals, long id, int serverId, ShopType shopType, int discount, int numMultiple, long startTime, long endTime) {
		this(sGlobals);
		this.id = id;
		this.serverId = serverId;
		this.shopType = shopType;
		this.discount = discount;
		this.numMultiple = numMultiple;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * 从数据库加载的时候调用
	 * @param sGlobals
	 */
	public ShopDiscount(ServerGlobals sGlobals) {
		super(sGlobals);
	}
	
	@Override
	public Long getDbId() {
		return this.id;
	}

	@Override
	public ShopDiscountEntity toEntity() {
		ShopDiscountEntity entity = new ShopDiscountEntity();
		entity.setId(id);
		entity.setServerId(serverId);
		entity.setShopTypeId(this.shopType.getIndex());
		entity.setDiscount(discount);
		entity.setNumMultiple(numMultiple);
		entity.setStartTime(new Timestamp(startTime));
		entity.setEndTime(new Timestamp(endTime));
		return entity;
	}

	@Override
	public void fromEntity(ShopDiscountEntity entity) {
		this.id = entity.getId();
		this.serverId = entity.getServerId();
		this.shopType = ShopType.getByIndex(entity.getShopTypeId());
		this.discount = entity.getDiscount();
		this.numMultiple = entity.getNumMultiple();
		this.startTime = entity.getStartTime().getTime();
		this.endTime = entity.getEndTime().getTime();
	}
	
	/**
	 * 打折活动是否已经结束
	 * @return
	 */
	public boolean isEnded() {
		return Globals.getTimeService().now() >= this.endTime;
	}
	
	/**
	 * 是否正在打折中
	 * @return
	 */
	public boolean isDiscounting() {
		long now = Globals.getTimeService().now();
		return now >= this.startTime && now < this.endTime;
	}

	public long getId() {
		return id;
	}

	public int getServerId() {
		return serverId;
	}

	public ShopType getShopType() {
		return shopType;
	}

	public int getDiscount() {
		return discount;
	}

	public int getNumMultiple() {
		return numMultiple;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

}
