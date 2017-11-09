package com.mokylin.bleach.gamedb.orm.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IServerRelatedEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ShopDiscountKey;

/**
 * 物品打折实体
 * @author yaguang.xiao
 *
 */
@Entity
@Table(name = "t_shop_discount")
public class ShopDiscountEntity implements EntityWithRedisKey<ShopDiscountKey>, IServerRelatedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** UUId */
	private long id;
	/** 服务器Id */
	private int serverId;
	/** 物品商店类型 */
	private int shopTypeId;
	/** 折扣(万分数) */
	private int discount;
	/** 数量倍数 */
	private int numMultiple;
	/** 折扣开始时间 */
	private Timestamp startTime;
	/** 折扣结束时间 */
	private Timestamp endTime;

	@Override
	public ShopDiscountKey newRedisKey(Integer serverId) {
		return new ShopDiscountKey(serverId, id, this.shopTypeId);
	}

	@Id
	@Column
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column
	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column
	public int getShopTypeId() {
		return shopTypeId;
	}

	public void setShopTypeId(int shopTypeId) {
		this.shopTypeId = shopTypeId;
	}

	@Column
	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	@Column
	public int getNumMultiple() {
		return numMultiple;
	}

	public void setNumMultiple(int numMultiple) {
		this.numMultiple = numMultiple;
	}

	@Column
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	@Column
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	@Override
	public int serverId() {
		return this.serverId;
	}

}
