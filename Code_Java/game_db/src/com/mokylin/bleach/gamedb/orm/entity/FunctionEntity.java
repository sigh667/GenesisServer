package com.mokylin.bleach.gamedb.orm.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IHumanRelatedEntity;
import com.mokylin.bleach.gamedb.redis.key.model.FunctionKey;

@Entity
@Table(name = "t_function")
public class FunctionEntity implements EntityWithRedisKey<FunctionKey>, IHumanRelatedEntity {

	private static final long serialVersionUID = 1L;
	
	/** 主键Id */
	private long id;
	/** 玩家Id */
	private long humanId;
	/** 功能Id */
	private int functionId;
	/** 打开时间 */
	private Timestamp openTime;

	@Override
	public long humanId() {
		return this.humanId;
	}

	@Override
	public FunctionKey newRedisKey(Integer serverId) {
		return new FunctionKey(serverId, humanId, functionId, id);
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
	public long getHumanId() {
		return humanId;
	}

	public void setHumanId(long humanId) {
		this.humanId = humanId;
	}

	@Column
	public int getFunctionId() {
		return functionId;
	}

	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	@Column
	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

}
