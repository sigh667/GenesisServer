package com.mokylin.bleach.common.prop.battleprop.propeffect;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
/**
 * 属性作用标识工厂
 * @author yaguang.xiao
 *
 */
public class PEIdFactory {

	/** 属性作用标识 */
	private static final Table<HeroBattlePropId, PropEffectType, PEIdentifier> ids = HashBasedTable.create();
	
	/**
	 * 初始化属性作用标识
	 */
	static{
		for(HeroBattlePropId id : HeroBattlePropId.values()) {
			for(PropEffectType effectType : PropEffectType.values()) {
				ids.put(id, effectType, new PEIdentifier(id, effectType));
			}
		}
	}

	public static void init() {}
	
	/**
	 * 获取指定的属性作用标识对象
	 * @param id
	 * @param effectType
	 * @return
	 */
	public static PEIdentifier get(HeroBattlePropId id, PropEffectType effectType) {
		return ids.get(id, effectType);
	}
}
