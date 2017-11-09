package com.mokylin.bleach.gamedb.orm.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IHumanRelatedEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ShopKey;

/**
 * 商店
 * @author yaguang.xiao
 *
 */
@Entity
@Table(name = "t_shop")
public class ShopEntity implements EntityWithRedisKey<ShopKey>, IHumanRelatedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 商店UUId */
	private long id;
	/** 玩家角色Id */
	private long humanId;
	/** 商店类型 */
	private int shopType;
	/** 货物刷出来的时间 */
	private long goodBornTime;
	/** 获取列表 */
	private String goodDataList;
	/** 手动刷新次数 */
	private int manuallyRefreshCount;
	/** 上次重置手动刷新次数时间 */
	private Timestamp lastResetManuallyRefreshCountTime;
	/** 上次自动刷新时间 */
	private Timestamp lastAutoRefreshTime;
	/** 是否永久开放（1：永久开放， 0：非永久开放） */
	private int isOpenForever;
	/** 商店关闭时间（商店不是永久开放时此字段才有效） */
	private Timestamp closeTime;

	@Override
	public ShopKey newRedisKey(Integer serverId) {
		return new ShopKey(serverId, this.humanId, this.shopType, this.id);
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
	public int getShopType() {
		return shopType;
	}

	public void setShopType(int shopType) {
		this.shopType = shopType;
	}

	@Column
	public long getGoodBornTime() {
		return goodBornTime;
	}

	public void setGoodBornTime(long goodBornTime) {
		this.goodBornTime = goodBornTime;
	}

	@Column(columnDefinition = "text")
	public String getGoodDataList() {
		return goodDataList;
	}

	public void setGoodDataList(String goodDataList) {
		this.goodDataList = goodDataList;
	}

	@Column
	public int getManuallyRefreshCount() {
		return manuallyRefreshCount;
	}

	public void setManuallyRefreshCount(int manuallyRefreshCount) {
		this.manuallyRefreshCount = manuallyRefreshCount;
	}

	@Column
	public Timestamp getLastResetManuallyRefreshCountTime() {
		return lastResetManuallyRefreshCountTime;
	}

	public void setLastResetManuallyRefreshCountTime(Timestamp lastResetManuallyRefreshCountTime) {
		this.lastResetManuallyRefreshCountTime = lastResetManuallyRefreshCountTime;
	}

	@Column
	public Timestamp getLastAutoRefreshTime() {
		return lastAutoRefreshTime;
	}

	public void setLastAutoRefreshTime(Timestamp lastAutoRefreshTime) {
		this.lastAutoRefreshTime = lastAutoRefreshTime;
	}

	@Column
	public int getIsOpenForever() {
		return isOpenForever;
	}

	public void setIsOpenForever(int isOpenForever) {
		this.isOpenForever = isOpenForever;
	}

	@Column
	public Timestamp getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
	}

	@Override
	public long humanId() {
		return this.humanId;
	}
	
	@Column
	public long getHumanId() {
		return humanId;
	}
	
	public void setHumanId(long humanId) {
		this.humanId = humanId;
	}

}
