package com.mokylin.bleach.gamedb.orm.entity;

import javax.persistence.Column;
import javax.persistence.Id;

import com.mokylin.bleach.core.orm.BaseEntity;


/**
 * 此类只在Redis中存储用，数据库中没有此类对应的表
 * @author baoliang.shen
 *
 */
public class AccountEntity implements BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**账号ID*/
	private String id;
	/**渠道*/
	private String channel;
	/**是否GM账号 TODO*/
	
	public AccountEntity(){}

	@Id
	@Column
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Column
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return channel+id;
	}
}
