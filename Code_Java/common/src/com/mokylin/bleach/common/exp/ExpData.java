package com.mokylin.bleach.common.exp;

/**
 * 经验等级包装类
 * @author ChangXiao
 * 
 */
public class ExpData {

	private int level;
	private long exp;

	public ExpData() {
	}

	/**
	 * ExpData
	 * 
	 * @param long level
	 * @param long exp
	 */
	public ExpData(int level, long exp) {
		this.level = level;
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public long getExp() {
		return exp;
	}

	@Override
	public String toString() {
		return "Level:" + level + ", Exp:" + exp;
	}

}
