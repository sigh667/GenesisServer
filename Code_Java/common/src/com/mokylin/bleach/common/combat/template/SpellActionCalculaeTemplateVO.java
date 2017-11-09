package com.mokylin.bleach.common.combat.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import java.util.List;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * Spell的战斗计算模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class SpellActionCalculaeTemplateVO extends TemplateObject {
	
	/** 伤害百分比 */
	@ExcelCellBinding(offset = 2)
	protected float attackRate;

	/** 给被击方的Buff */
	@ExcelCellBinding(offset = 3)
	protected String strTargetBuffIds;

	/** 给攻击方的Buff */
	@ExcelCellBinding(offset = 4)
	protected String strCasterBuffIds;

	/** 效果器 */
	@ExcelCollectionMapping(clazz = com.mokylin.bleach.common.combat.model.EffectBase.class, collectionNumber = "5,6,7,8,9,10;11,12,13,14,15,16;17,18,19,20,21,22")
	protected List<com.mokylin.bleach.common.combat.model.EffectBase> attributeList;


	public float getAttackRate() {
		return this.attackRate;
	}

	public void setAttackRate(float attackRate) {
		if (attackRate == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[伤害百分比]attackRate不可以为0");
		}
		this.attackRate = attackRate;
	}
	
	public String getStrTargetBuffIds() {
		return this.strTargetBuffIds;
	}

	public void setStrTargetBuffIds(String strTargetBuffIds) {
		this.strTargetBuffIds = strTargetBuffIds;
	}
	
	public String getStrCasterBuffIds() {
		return this.strCasterBuffIds;
	}

	public void setStrCasterBuffIds(String strCasterBuffIds) {
		this.strCasterBuffIds = strCasterBuffIds;
	}
	
	public List<com.mokylin.bleach.common.combat.model.EffectBase> getAttributeList() {
		return this.attributeList;
	}

	public void setAttributeList(List<com.mokylin.bleach.common.combat.model.EffectBase> attributeList) {
		if (attributeList == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[效果器]attributeList不可以为空");
		}	
		this.attributeList = attributeList;
	}
	

	@Override
	public String toString() {
		return "SpellActionCalculaeTemplateVO[attackRate=" + attackRate + ",strTargetBuffIds=" + strTargetBuffIds + ",strCasterBuffIds=" + strCasterBuffIds + ",attributeList=" + attributeList + ",]";

	}
}