package com.mokylin.td.network2client.core.floodfilter;

/**
 * 洪水攻击的全局控制开关
 * @author baoliang.shen
 *
 */
public enum FloodConstants {
	Inst;

	private boolean isOpenFloodFilter;

	public boolean isOpenFloodFilter() {
		return isOpenFloodFilter;
	}
	public void setOpenFloodFilter(boolean isOpenFloodFilter) {
		this.isOpenFloodFilter = isOpenFloodFilter;
	}
}
