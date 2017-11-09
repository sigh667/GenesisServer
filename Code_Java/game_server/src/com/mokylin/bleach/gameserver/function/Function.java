package com.mokylin.bleach.gameserver.function;

import java.sql.Timestamp;

import com.mokylin.bleach.common.function.FunctionType;
import com.mokylin.bleach.gamedb.orm.entity.FunctionEntity;
import com.mokylin.bleach.gameserver.core.persistance.ObjectInSqlImpl;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 功能逻辑对象
 * @author yaguang.xiao
 *
 */
public class Function extends ObjectInSqlImpl<Long, FunctionEntity> {

	private Human human;
	private long id;
	private FunctionType funcType;
	private Timestamp openTime;
	
	/**
	 * 从数据库中加载数据时调用的方法
	 * @param human
	 */
	public Function(Human human) {
		super(human.getDataUpdater());
		this.human = human;
	}
	
	/**
	 * 开启功能时调用的方法
	 * @param human
	 * @param id
	 * @param funcType
	 * @param openTime
	 */
	public Function(Human human, long id, FunctionType funcType, Timestamp openTime) {
		this(human);
		this.id = id;
		this.funcType = funcType;
		this.openTime = openTime;
	}

	@Override
	public Long getDbId() {
		return id;
	}

	@Override
	public FunctionEntity toEntity() {
		FunctionEntity entity = new FunctionEntity();
		entity.setId(id);
		entity.setHumanId(human.getId());
		entity.setFunctionId(this.funcType.getIndex());
		entity.setOpenTime(openTime);
		return entity;
	}

	@Override
	public void fromEntity(FunctionEntity entity) {
		this.id = entity.getId();
		this.funcType = FunctionType.valueOf(entity.getFunctionId());
		this.openTime = entity.getOpenTime();
	}
	
	public FunctionType getType() {
		return this.funcType;
	}

}
