package com.mokylin.bleach.common.prop.battleprop.propeffect;

import java.util.ArrayList;
import java.util.List;

import com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col;
import com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;

/**
 * 负责将Excel中的临时作用对象转换为BattlePropEffect
 * @author baoliang.shen
 *
 */
public enum PropEffectConverter {
	Inst
	;


	/**
	 * 从TempAttrNode对象转换为BattlePropEffect的List<p>
	 * 转换好的List中都是有效值
	 * @param TempAttrNode3Col
	 * @return
	 */
	public List<BattlePropEffect> convertToBattlePropEffect(TempAttrNode3Col tempAttrNode) {
		List<BattlePropEffect> list = new ArrayList<BattlePropEffect>();
		HeroBattlePropId id = tempAttrNode.getAttributeIndex();
		if (tempAttrNode.getAbsValue()!=0) {
			BattlePropEffect effect = new BattlePropEffect(id, PropEffectType.Abs, tempAttrNode.getAbsValue());
			list.add(effect);
		}
		if (tempAttrNode.getPerValue()!=0) {
			BattlePropEffect effect = new BattlePropEffect(id, PropEffectType.Per, tempAttrNode.getPerValue());
			list.add(effect);
		}

		return list;
	}

	/**
	 * 将TempAttrNode2对象转换为BattlePropEffect
	 * @param tempAttrNode
	 * @return
	 */
	public BattlePropEffect convertToBattlePropEffect(TempAttrNode2Col tempAttrNode) {
		String attributeIndex = tempAttrNode.getAttributeIndex();
		if (attributeIndex==null || attributeIndex.isEmpty()) {
			return null;
		}

		HeroBattlePropId id = HeroBattlePropId.valueOf(attributeIndex);
		if (tempAttrNode.getAttributeValue()!=0) {
			BattlePropEffect effect = new BattlePropEffect(id, PropEffectType.Abs, tempAttrNode.getAttributeValue());
			return effect;
		}

		return null;
	}
}
