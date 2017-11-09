package com.mokylin.bleach.gamedb.orm.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IHumanRelatedEntity;
import com.mokylin.bleach.gamedb.redis.key.model.ItemKey;

@Entity
@Table(name = "t_item")
public class ItemEntity implements EntityWithRedisKey<ItemKey>, IHumanRelatedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**道具ID*/
	private Long id;
	/**所属角色ID*/
	private long humanId;
	/**道具模板ID*/
	private int templateId;
	/**叠加数量*/
	private int overlap;
	/**创建时间*/
	private Timestamp createTime;

	@Override
	public ItemKey newRedisKey(Integer serverId) {
		return new ItemKey(serverId, this.humanId(), this.getId());
	}

	@Id
	@Column
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long humanId() {
		return humanId;
	}
	
	@Column
	public long getHumanId() {
		return humanId;
	}

	public void setHumanId(long humanId) {
		this.humanId = humanId;
	}

	@Column
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	@Column
	public int getOverlap() {
		return overlap;
	}

	public void setOverlap(int overlap) {
		this.overlap = overlap;
	}

	@Column
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
