package com.mokylin.bleach.common.combat.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import java.util.List;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * Spell模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class SpellTemplateVO extends TemplateObject {
	
	/** Icon */
	@ExcelCellBinding(offset = 2)
	protected int icon;

	/** 名称 */
	@ExcelCellBinding(offset = 3)
	protected String name;

	/** 描述 */
	@ExcelCellBinding(offset = 4)
	protected String desc;

	/** 优先级 */
	@ExcelCellBinding(offset = 5)
	protected int priority;

	/** MP消耗 */
	@ExcelCellBinding(offset = 6)
	protected int mpCost;

	/** CD（单位：回合数） */
	@ExcelCellBinding(offset = 7)
	protected int cd;

	/** 目标选择方式 */
	@ExcelCellBinding(offset = 8)
	protected com.mokylin.bleach.common.combat.enumeration.TargetSelectorType targetSelector;

	/** 技能范围 */
	@ExcelCellBinding(offset = 9)
	protected com.mokylin.bleach.common.combat.enumeration.RangeSelectorType rangeSelector;

	/** 元素类型 */
	@ExcelCellBinding(offset = 10)
	protected com.mokylin.bleach.common.combat.enumeration.YuanSuLeiXing yuanSuLeiXing;

	/** 招式类型 */
	@ExcelCellBinding(offset = 11)
	protected com.mokylin.bleach.common.combat.enumeration.SpellType spellType;

	/** 是否显示 */
	@ExcelCellBinding(offset = 12)
	protected boolean isVisible;

	/** 招式伤害类型 */
	@ExcelCellBinding(offset = 13)
	protected com.mokylin.bleach.common.combat.enumeration.SpellDamageType spellDamageType;

	/** 招式结算公式类型 */
	@ExcelCellBinding(offset = 14)
	protected com.mokylin.bleach.common.combat.enumeration.SpellExpType spellExpType;

	/** 技能等级相关系数 */
	@ExcelCellBinding(offset = 15)
	protected float levelRelatedParameter;

	/** 技能修正值(万分比) */
	@ExcelCellBinding(offset = 16)
	protected int iSpellCorrectionParameter;

	/** 是否必中 */
	@ExcelCellBinding(offset = 17)
	protected boolean IsBiZhong;

	/** 是否必暴击 */
	@ExcelCellBinding(offset = 18)
	protected boolean IsBiBaoJi;

	/** 是否不判定暴击 */
	@ExcelCellBinding(offset = 19)
	protected boolean IsNoBaoJi;

	/** 是否无法格挡 */
	@ExcelCellBinding(offset = 20)
	protected boolean IsWuFaGeDang;

	/** 是否必破甲 */
	@ExcelCellBinding(offset = 21)
	protected boolean IsBiPoJia;

	/** 射程 */
	@ExcelCellBinding(offset = 23)
	protected float AttackRange;

	/** 伤害计算事件ID */
	@ExcelCellBinding(offset = 25)
	protected String strActionID;

	/** 附带的属性 */
	@ExcelCollectionMapping(clazz = com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col.class, collectionNumber = "30,31;32,33;34,35;36,37;38,39")
	protected List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col> attributeList;


	public int getIcon() {
		return this.icon;
	}

	public void setIcon(int icon) {
		if (icon == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[Icon]icon不可以为0");
		}
		this.icon = icon;
	}
	
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
	
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		if (StringUtils.isEmpty(desc)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[描述]desc不可以为空");
		}
		this.desc = desc;
	}
	
	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		if (priority == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[优先级]priority不可以为0");
		}
		this.priority = priority;
	}
	
	public int getMpCost() {
		return this.mpCost;
	}

	public void setMpCost(int mpCost) {
		this.mpCost = mpCost;
	}
	
	public int getCd() {
		return this.cd;
	}

	public void setCd(int cd) {
		this.cd = cd;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.TargetSelectorType getTargetSelector() {
		return this.targetSelector;
	}

	public void setTargetSelector(com.mokylin.bleach.common.combat.enumeration.TargetSelectorType targetSelector) {
		if (targetSelector == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[目标选择方式]targetSelector不可以为空");
		}	
		this.targetSelector = targetSelector;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.RangeSelectorType getRangeSelector() {
		return this.rangeSelector;
	}

	public void setRangeSelector(com.mokylin.bleach.common.combat.enumeration.RangeSelectorType rangeSelector) {
		if (rangeSelector == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[技能范围]rangeSelector不可以为空");
		}	
		this.rangeSelector = rangeSelector;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.YuanSuLeiXing getYuanSuLeiXing() {
		return this.yuanSuLeiXing;
	}

	public void setYuanSuLeiXing(com.mokylin.bleach.common.combat.enumeration.YuanSuLeiXing yuanSuLeiXing) {
		if (yuanSuLeiXing == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[元素类型]yuanSuLeiXing不可以为空");
		}	
		this.yuanSuLeiXing = yuanSuLeiXing;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.SpellType getSpellType() {
		return this.spellType;
	}

	public void setSpellType(com.mokylin.bleach.common.combat.enumeration.SpellType spellType) {
		if (spellType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[招式类型]spellType不可以为空");
		}	
		this.spellType = spellType;
	}
	
	public boolean isIsVisible() {
		return this.isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.SpellDamageType getSpellDamageType() {
		return this.spellDamageType;
	}

	public void setSpellDamageType(com.mokylin.bleach.common.combat.enumeration.SpellDamageType spellDamageType) {
		if (spellDamageType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[招式伤害类型]spellDamageType不可以为空");
		}	
		this.spellDamageType = spellDamageType;
	}
	
	public com.mokylin.bleach.common.combat.enumeration.SpellExpType getSpellExpType() {
		return this.spellExpType;
	}

	public void setSpellExpType(com.mokylin.bleach.common.combat.enumeration.SpellExpType spellExpType) {
		if (spellExpType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[招式结算公式类型]spellExpType不可以为空");
		}	
		this.spellExpType = spellExpType;
	}
	
	public float getLevelRelatedParameter() {
		return this.levelRelatedParameter;
	}

	public void setLevelRelatedParameter(float levelRelatedParameter) {
		this.levelRelatedParameter = levelRelatedParameter;
	}
	
	public int getISpellCorrectionParameter() {
		return this.iSpellCorrectionParameter;
	}

	public void setISpellCorrectionParameter(int iSpellCorrectionParameter) {
		if (iSpellCorrectionParameter == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[技能修正值(万分比)]iSpellCorrectionParameter不可以为0");
		}
		this.iSpellCorrectionParameter = iSpellCorrectionParameter;
	}
	
	public boolean isIsBiZhong() {
		return this.IsBiZhong;
	}

	public void setIsBiZhong(boolean IsBiZhong) {
		this.IsBiZhong = IsBiZhong;
	}
	
	public boolean isIsBiBaoJi() {
		return this.IsBiBaoJi;
	}

	public void setIsBiBaoJi(boolean IsBiBaoJi) {
		this.IsBiBaoJi = IsBiBaoJi;
	}
	
	public boolean isIsNoBaoJi() {
		return this.IsNoBaoJi;
	}

	public void setIsNoBaoJi(boolean IsNoBaoJi) {
		this.IsNoBaoJi = IsNoBaoJi;
	}
	
	public boolean isIsWuFaGeDang() {
		return this.IsWuFaGeDang;
	}

	public void setIsWuFaGeDang(boolean IsWuFaGeDang) {
		this.IsWuFaGeDang = IsWuFaGeDang;
	}
	
	public boolean isIsBiPoJia() {
		return this.IsBiPoJia;
	}

	public void setIsBiPoJia(boolean IsBiPoJia) {
		this.IsBiPoJia = IsBiPoJia;
	}
	
	public float getAttackRange() {
		return this.AttackRange;
	}

	public void setAttackRange(float AttackRange) {
		if (AttackRange == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[射程]AttackRange不可以为0");
		}
		this.AttackRange = AttackRange;
	}
	
	public String getStrActionID() {
		return this.strActionID;
	}

	public void setStrActionID(String strActionID) {
		this.strActionID = strActionID;
	}
	
	public List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col> getAttributeList() {
		return this.attributeList;
	}

	public void setAttributeList(List<com.mokylin.bleach.common.core.excelmodel.TempAttrNode2Col> attributeList) {
		if (attributeList == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[附带的属性]attributeList不可以为空");
		}	
		this.attributeList = attributeList;
	}
	

	@Override
	public String toString() {
		return "SpellTemplateVO[icon=" + icon + ",name=" + name + ",desc=" + desc + ",priority=" + priority + ",mpCost=" + mpCost + ",cd=" + cd + ",targetSelector=" + targetSelector + ",rangeSelector=" + rangeSelector + ",yuanSuLeiXing=" + yuanSuLeiXing + ",spellType=" + spellType + ",isVisible=" + isVisible + ",spellDamageType=" + spellDamageType + ",spellExpType=" + spellExpType + ",levelRelatedParameter=" + levelRelatedParameter + ",iSpellCorrectionParameter=" + iSpellCorrectionParameter + ",IsBiZhong=" + IsBiZhong + ",IsBiBaoJi=" + IsBiBaoJi + ",IsNoBaoJi=" + IsNoBaoJi + ",IsWuFaGeDang=" + IsWuFaGeDang + ",IsBiPoJia=" + IsBiPoJia + ",AttackRange=" + AttackRange + ",strActionID=" + strActionID + ",attributeList=" + attributeList + ",]";

	}
}