package com.mokylin.bleach.common.human;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.human.template.CostByCountsTemplate;
/**
 * CostByCounts产品类型枚举（购买花销根据购买次数变化）
 * 
 * @author ChangXiao
 *
 */
public enum CostByCountsType {
	/** 体力 */
	ENERGY,
	/** 点金手 */
	BUY_GOLD,
	;
	
	
	/**
	 * 获取购买次数对应的钻石消耗
	 * @param count 购买次数
	 * @return 消耗的钻石数量
	 */
	public long getCostByCounts(int count){
		if (count <= 0 || count > size()) {
			throw new IllegalArgumentException(String.format("购买次数=%d (购买次数必须>0, 且<=已定义的最高次数 %d)", count, size()));
		}
		return GlobalData.getTemplateService().get(count, CostByCountsTemplate.class).getCostByCountsArray()[ordinal()];
	}
	
	/**
	 * 该类型已配置的次数
	 * @return
	 */
	public int size(){
		return CostByCountsTemplate.getLength(this);
	}
}
