package com.mokylin.bleach.common.prop.battleprop.source;

import com.mokylin.bleach.core.annotation.InternalUse;

/**
 * 属性来源
 * 
 * @author yaguang.xiao
 * 
 */
@InternalUse
public class PropSource {

	/** 属性来源类型 */
	private final PropSourceType type;
	/** 属性来源Id */
	private final long id;

	public PropSource(PropSourceType type, long id) {
		this.type = type;
		this.id = id;
	}

	/**
	 * 获取属性来源类型
	 * @return
	 */
	public PropSourceType getType() {
		return type;
	}

	/**
	 * 获取属性来源Id
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * 是否有效
	 * @return
	 */
	public boolean isValid() {
		if(this.type == null) {
			return false;
		}
		
		return true;
	}

}
