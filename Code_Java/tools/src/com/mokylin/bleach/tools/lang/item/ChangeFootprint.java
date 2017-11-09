package com.mokylin.bleach.tools.lang.item;

import com.mokylin.bleach.core.annotation.ThreadSafe;

/**
 * 文字的修改足迹
 * 
 * @author yaguang.xiao
 * 
 */
@ThreadSafe
public class ChangeFootprint {

	private final int version;
	private final int id;

	public ChangeFootprint(int version, int id) {
		this.version = version;
		this.id = id;
	}
	
	public int getVersion() {
		return version;
	}

	public int getId() {
		return id;
	}

}
