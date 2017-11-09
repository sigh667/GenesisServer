package com.mokylin.bleach.common.hero.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.annotation.ExcelCollectionMapping;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * hero模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class HeroAttrTemplateVO extends TemplateObject {
	
	/** 英雄组ID */
	@ExcelCellBinding(offset = 1)
	protected int heroGroupId;

	/** 名称 */
	@ExcelCellBinding(offset = 2)
	protected String name;

	/** 描述 */
	@ExcelCellBinding(offset = 3)
	protected String desc;

	/** Icon */
	@ExcelCellBinding(offset = 4)
	protected String icon;

	/** 升品后英雄ID */
	@ExcelCellBinding(offset = 5)
	protected int nextHeroId;

	/** 品质 */
	@ExcelCellBinding(offset = 6)
	protected int qualityId;

	/** 升品消耗金币 */
	@ExcelCellBinding(offset = 7)
	protected long qualityUpCost;

	/** 种族ID */
	@ExcelCellBinding(offset = 8)
	protected int raceId;

	/** 普攻技能ID */
	@ExcelCellBinding(offset = 9)
	protected int normalSkillId;

	/** 斩魄刀技ID */
	@ExcelCellBinding(offset = 10)
	protected int specialSkillId;

	/** 辅助技ID */
	@ExcelCollectionMapping(clazz = int.class, collectionNumber = "11;12;13;14;15")
	protected int[] secondarySkillIds;

	/** 装备ID */
	@ExcelCollectionMapping(clazz = int.class, collectionNumber = "16;17;18;19;20;21")
	protected int[] equipIds;

	/** 对应的role_attr文件中的ID */
	@ExcelCellBinding(offset = 22)
	protected int roleAttrId;


	public int getHeroGroupId() {
		return this.heroGroupId;
	}

	public void setHeroGroupId(int heroGroupId) {
		if (heroGroupId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[英雄组ID]heroGroupId不可以为0");
		}
		this.heroGroupId = heroGroupId;
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
	
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		if (StringUtils.isEmpty(icon)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[Icon]icon不可以为空");
		}
		this.icon = icon;
	}
	
	public int getNextHeroId() {
		return this.nextHeroId;
	}

	public void setNextHeroId(int nextHeroId) {
		this.nextHeroId = nextHeroId;
	}
	
	public int getQualityId() {
		return this.qualityId;
	}

	public void setQualityId(int qualityId) {
		if (qualityId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[品质]qualityId不可以为0");
		}
		if (qualityId > 20 || qualityId < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[品质]qualityId的值不合法，应为1至20之间");
		}
		this.qualityId = qualityId;
	}
	
	public long getQualityUpCost() {
		return this.qualityUpCost;
	}

	public void setQualityUpCost(long qualityUpCost) {
		if (qualityUpCost < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[升品消耗金币]qualityUpCost的值不得小于0");
		}
		this.qualityUpCost = qualityUpCost;
	}
	
	public int getRaceId() {
		return this.raceId;
	}

	public void setRaceId(int raceId) {
		if (raceId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[种族ID]raceId不可以为0");
		}
		if (raceId > 7 || raceId < 1) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[种族ID]raceId的值不合法，应为1至7之间");
		}
		this.raceId = raceId;
	}
	
	public int getNormalSkillId() {
		return this.normalSkillId;
	}

	public void setNormalSkillId(int normalSkillId) {
		if (normalSkillId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[普攻技能ID]normalSkillId不可以为0");
		}
		this.normalSkillId = normalSkillId;
	}
	
	public int getSpecialSkillId() {
		return this.specialSkillId;
	}

	public void setSpecialSkillId(int specialSkillId) {
		if (specialSkillId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[斩魄刀技ID]specialSkillId不可以为0");
		}
		this.specialSkillId = specialSkillId;
	}
	
	public int[] getSecondarySkillIds() {
		return this.secondarySkillIds;
	}

	public void setSecondarySkillIds(int[] secondarySkillIds) {
		if (secondarySkillIds == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[辅助技ID]secondarySkillIds不可以为空");
		}	
		this.secondarySkillIds = secondarySkillIds;
	}
	
	public int[] getEquipIds() {
		return this.equipIds;
	}

	public void setEquipIds(int[] equipIds) {
		if (equipIds == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[装备ID]equipIds不可以为空");
		}	
		this.equipIds = equipIds;
	}
	
	public int getRoleAttrId() {
		return this.roleAttrId;
	}

	public void setRoleAttrId(int roleAttrId) {
		if (roleAttrId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[对应的role_attr文件中的ID]roleAttrId不可以为0");
		}
		this.roleAttrId = roleAttrId;
	}
	

	@Override
	public String toString() {
		return "HeroAttrTemplateVO[heroGroupId=" + heroGroupId + ",name=" + name + ",desc=" + desc + ",icon=" + icon + ",nextHeroId=" + nextHeroId + ",qualityId=" + qualityId + ",qualityUpCost=" + qualityUpCost + ",raceId=" + raceId + ",normalSkillId=" + normalSkillId + ",specialSkillId=" + specialSkillId + ",secondarySkillIds=" + secondarySkillIds + ",equipIds=" + equipIds + ",roleAttrId=" + roleAttrId + ",]";

	}
}