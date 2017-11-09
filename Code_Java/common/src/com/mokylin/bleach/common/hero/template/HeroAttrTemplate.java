package com.mokylin.bleach.common.hero.template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col;
import com.mokylin.bleach.common.hero.HeroConstants;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectConverter;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * Hero基础属性表
 * @author baoliang.shen
 *
 */
@ExcelRowBinding
public class HeroAttrTemplate extends HeroAttrTemplateVO {
	
	/**此Hero在0级时的初始属性加成*/
	private List<BattlePropEffect> newPropEffects;

	@Override
	public void check() throws TemplateConfigException {
		if (equipIds.length!=HeroConstants.EquipCount) {
			throw new TemplateConfigException(this.getSheetName(), getId(),
					String.format("装备数量%d非法", equipIds.length));
		}
		
		if (hasNextQualityHero()) {
			//可以升品
			//那么检查下一品的英雄是否存在
			HeroAttrTemplate nextTemplate = GlobalData.getTemplateService().get(nextHeroId, HeroAttrTemplate.class);
			if (nextTemplate==null) {
				throw new TemplateConfigException(this.getSheetName(), getId(),
						String.format("升品后英雄ID【%d】非法", nextHeroId));
			}
			
			//检查升品所需金币
			if (this.qualityUpCost <= 0) {
				throw new TemplateConfigException(this.getSheetName(), getId(),
						String.format("升品消耗金币【%d】非法", qualityUpCost));
			}
		}
		
		//检查辅助技是否重复
		HashSet<Integer> set = new HashSet<>();
		for (int skillId : secondarySkillIds) {
			if (skillId==0)
				continue;

			if (set.contains(skillId)) {
				throw new TemplateConfigException(this.getSheetName(), getId(),
						String.format("辅助技ID【%d】重复", skillId));
			}
			set.add(skillId);
		}
		
		//检查secondarySkillIds里所有非0的id必须存在于技能表 TODO
		
	}

	@Override
	public void patchUp() throws Exception {
		RoleAttrTemplate roletemplate = GlobalData.getTemplateService().get(this.getRoleAttrId(), RoleAttrTemplate.class);
		List<TempAttrNode3Col> attributeList = roletemplate.getAttributeList();
		newPropEffects = new ArrayList<>();
		for (TempAttrNode3Col tempAttrNode : attributeList) {
			final List<BattlePropEffect> tempList = PropEffectConverter.Inst.convertToBattlePropEffect(tempAttrNode);
			newPropEffects.addAll(tempList);
		}
	}

	/**
	 * 是否有下一品质的Hero
	 * @return
	 */
	public boolean hasNextQualityHero() {
		return nextHeroId>0;
	}

	public List<BattlePropEffect> getNewPropEffects() {
		return newPropEffects;
	}

}
