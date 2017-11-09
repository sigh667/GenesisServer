package com.mokylin.bleach.common.combat.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import java.util.List;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelObjectMapping;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * Buff模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class BuffTemplateVO extends TemplateObject {
	
	/** 名称 */
	@ExcelCellBinding(offset = 1)
	protected String name;

	/** WorkingTime(作用时间) */
	@ExcelCellBinding(offset = 5)
	protected com.mokylin.bleach.common.combat.enumeration.BuffWorkingTime workingTime;

	/** 持续回合数 */
	@ExcelCellBinding(offset = 6)
	protected int remainTime;

	/** 正负级别 */
	@ExcelCellBinding(offset = 7)
	protected com.mokylin.bleach.common.combat.enumeration.BuffType buffType;

	/** 可否被驱散 */
	@ExcelCellBinding(offset = 8)
	protected boolean isCanStop;

	/** Buff命中规则 */
	@ExcelCellBinding(offset = 9)
	protected com.mokylin.bleach.common.combat.enumeration.SpellBuffHitType spellBuffHitType;

	/** 效果器 */
	@ExcelObjectMapping(fieldsNumber = "10,11,12,13,14,15")
	protected com.mokylin.bleach.common.combat.model.EffectBase effectMain;

	/** Buff结束时的效果器 */
	@ExcelObjectMapping(fieldsNumber = "16,17,18,19,20,21")
	protected com.mokylin.bleach.common.combat.model.EffectBase effectEnd;

	/** 附加的属性 */
	@ExcelCollectionMapping(clazz = com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col.class, collectionNumber = "22,23,24;25,26,27;28,29,30")
	protected List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col> attributeList;


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (StringUtils.isEmpty(name)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[名称]name不可以为空");
		}
		this.name = name;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.BuffWorkingTime getWorkingTime() {
		return this.workingTime;
	}

	public void setWorkingTime(com.mokylin.bleach.common.combat.enumeration.BuffWorkingTime workingTime) {
		if (workingTime == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[WorkingTime(作用时间)]workingTime不可以为空");
		}	
		this.workingTime = workingTime;
	}
	
	public int getRemainTime() {
		return this.remainTime;
	}

	public void setRemainTime(int remainTime) {
		if (remainTime == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[持续回合数]remainTime不可以为0");
		}
		this.remainTime = remainTime;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.BuffType getBuffType() {
		return this.buffType;
	}

	public void setBuffType(com.mokylin.bleach.common.combat.enumeration.BuffType buffType) {
		if (buffType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[正负级别]buffType不可以为空");
		}	
		this.buffType = buffType;
	}
	
	public boolean isIsCanStop() {
		return this.isCanStop;
	}

	public void setIsCanStop(boolean isCanStop) {
		this.isCanStop = isCanStop;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.SpellBuffHitType getSpellBuffHitType() {
		return this.spellBuffHitType;
	}

	public void setSpellBuffHitType(com.mokylin.bleach.common.combat.enumeration.SpellBuffHitType spellBuffHitType) {
		if (spellBuffHitType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[Buff命中规则]spellBuffHitType不可以为空");
		}	
		this.spellBuffHitType = spellBuffHitType;
	}
	
	public com.mokylin.bleach.common.combat.model.EffectBase getEffectMain() {
		return this.effectMain;
	}

	public void setEffectMain(com.mokylin.bleach.common.combat.model.EffectBase effectMain) {
		if (effectMain == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[效果器]effectMain不可以为空");
		}	
		this.effectMain = effectMain;
	}
	
	public com.mokylin.bleach.common.combat.model.EffectBase getEffectEnd() {
		return this.effectEnd;
	}

	public void setEffectEnd(com.mokylin.bleach.common.combat.model.EffectBase effectEnd) {
		if (effectEnd == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[Buff结束时的效果器]effectEnd不可以为空");
		}	
		this.effectEnd = effectEnd;
	}
	
	public List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col> getAttributeList() {
		return this.attributeList;
	}

	public void setAttributeList(List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col> attributeList) {
		if (attributeList == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[附加的属性]attributeList不可以为空");
		}	
		this.attributeList = attributeList;
	}
	

	@Override
	public String toString() {
		return "BuffTemplateVO[name=" + name + ",workingTime=" + workingTime + ",remainTime=" + remainTime + ",buffType=" + buffType + ",isCanStop=" + isCanStop + ",spellBuffHitType=" + spellBuffHitType + ",effectMain=" + effectMain + ",effectEnd=" + effectEnd + ",attributeList=" + attributeList + ",]";

	}
}