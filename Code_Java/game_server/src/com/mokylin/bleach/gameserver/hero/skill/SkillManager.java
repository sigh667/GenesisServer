package com.mokylin.bleach.gameserver.hero.skill;

import java.util.ArrayList;

import com.mokylin.bleach.gamedb.orm.vo.HeroSkill;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HeroMessage.HeroSkillInfo;

/**
 * Hero的技能管理器
 * @author baoliang.shen
 *
 */
public class SkillManager {

	/**角色对象*/
	private final Human owner;
	/**所属的Hero*/
	private final Hero hero;
	
	/**斩魄刀技*/
	private Skill specialSkill;
	/**辅助技*/
	private Skill[] skillArray = new Skill[SkillConstants.SkillCount];
	
	public SkillManager(Human human, Hero hero) {
		this.owner = human;
		this.hero = hero;
	}
	public Human getOwner() {
		return owner;
	}

	public HeroSkill[] buildHeroSkills() {
		HeroSkill[] heroSkills = new HeroSkill[SkillConstants.SkillCount+1];
		//斩魄刀技
		heroSkills[0] = specialSkill.convertToHeroSkill();
		//辅助技
		for (int i = 0; i < skillArray.length; i++) {
			final Skill skill = skillArray[i];
			if (skill==null)
				continue;
			
			final HeroSkill heroSkill = skill.convertToHeroSkill();
			heroSkills[i+1] = heroSkill;
		}
		return heroSkills;
	}
	public void initFromHeroSkills(HeroSkill[] heroSkills) {
		if (heroSkills==null) {
			throwExceptionWhenSpecialSkillIsNull();
		}

		//斩魄刀技
		if (heroSkills[0]==null) {
			throwExceptionWhenSpecialSkillIsNull();
		}
		specialSkill = Skill.buildFromHeroSkill(heroSkills[0],hero);
		if (specialSkill==null) {
			throwExceptionWhenSpecialSkillIsNull();
		}

		//辅助技
		for (int i = 1; i < heroSkills.length; i++) {
			final HeroSkill heroSkill = heroSkills[i];
			if (heroSkill==null)
				continue;

			final Skill skill = Skill.buildFromHeroSkill(heroSkill,hero);
			skillArray[i-1] = skill;
		}
	}
	
	private void throwExceptionWhenSpecialSkillIsNull() {
		String error = String.format("uuid【%d】的Hero从HeroEntity加载时，发现斩魄刀技竟然为null", hero.getDbId());
		throw new RuntimeException(error);
	}

	public Skill getSpecialSkill() {
		return specialSkill;
	}
	public Iterable<? extends HeroSkillInfo> buildSkillArrayInfos() {
		ArrayList<HeroSkillInfo> list = new ArrayList<>();
		for (int i = 0; i < skillArray.length; i++) {
			final Skill skill = skillArray[i];
			if (skill==null)
				continue;
			
			HeroSkillInfo skillInfo = skill.buildHeroSkillInfo();
			list.add(skillInfo);
		}
		return list;
	}

	public void giveInitialSkills() {
		refreshSkillByTemplate();
	}
	/**
	 * 根据模板重置技能的模板ID
	 */
	public void resetAllSkill() {
		refreshSkillByTemplate();
	}
	private void refreshSkillByTemplate() {
		//斩魄刀技
		final int templateId = hero.getTemplate().getSpecialSkillId();
		int specialSkillLevel = 1;
		if (specialSkill!=null) {
			specialSkillLevel = specialSkill.getLevel();
		}
		specialSkill = Skill.buildNewSkill(templateId,specialSkillLevel,hero);
		if (specialSkill==null) {
			throwExceptionWhenSpecialSkillIsNull();
		}
		
		//辅助技
		int skillLevel[] = new int[skillArray.length];
		for (int i = 0; i < skillArray.length; i++) {
			skillLevel[i] = 1;
			final Skill skill = skillArray[i];
			if (skill==null)
				continue;
			
			skillLevel[i] = skill.getLevel();
		}
		int[] secondarySkillIds = hero.getTemplate().getSecondarySkillIds();
		for (int i = 0; i < secondarySkillIds.length; i++) {
			if (secondarySkillIds[i] > 0) {
				final Skill newSkill = Skill.buildNewSkill(secondarySkillIds[i], skillLevel[i], hero);
				skillArray[i] = newSkill;
			}
		}
		
		hero.setModified();
	}

	public Skill getSkill(int skillTemplateId) {
		if (specialSkill.getTemplateId()==skillTemplateId) {
			return specialSkill;
		}
		for (int i = 0; i < skillArray.length; i++) {
			final Skill skill = skillArray[i];
			if (skill==null)
				continue;
			
			if (skill.getTemplateId()==skillTemplateId) {
				return skill;
			}
		}
		return null;
	}
}
