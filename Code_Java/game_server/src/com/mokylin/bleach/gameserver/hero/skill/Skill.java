package com.mokylin.bleach.gameserver.hero.skill;

import com.mokylin.bleach.gamedb.orm.vo.HeroSkill;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.protobuf.HeroMessage.HeroSkillInfo;
import com.mokylin.bleach.protobuf.HeroMessage.HeroSkillInfo.Builder;

/**
 * Hero身上的技能
 * @author baoliang.shen
 *
 */
public class Skill {
	
	private Hero hero;

	/**技能模板ID*/
	private int templateId;
	/**技能等级*/
	private int level;


	private Skill(){}
	public static Skill buildNewSkill(int templateId, int level, Hero hero) {
		Skill skill = new Skill();
		skill.templateId = templateId;
		skill.level = level;
		skill.hero = hero;
		return skill;
	}
	public static Skill buildFromHeroSkill(HeroSkill heroSkill, Hero hero) {
		Skill skill = new Skill();

		skill.templateId = heroSkill.getTemplateId();
		skill.level = heroSkill.getLevel();
		skill.hero = hero;

		return skill;
	}

	public HeroSkill convertToHeroSkill() {
		HeroSkill heroSkill = new HeroSkill();
		
		heroSkill.setTemplateId(templateId);
		heroSkill.setLevel(level);
		
		return heroSkill;
	}

	public HeroSkillInfo buildHeroSkillInfo() {
		Builder builder = HeroSkillInfo.newBuilder();
		builder.setLevel(level);
		builder.setTemplateId(templateId);
		return builder.build();
	}
	public int getTemplateId() {
		return templateId;
	}
	public int getLevel() {
		return level;
	}

	/**
	 * 技能升级
	 * @param levelUpCount	具体升几级
	 */
	public void levelUp(int levelUpCount) {
		if (levelUpCount<=0) {
			return;
		}
		
		this.level += levelUpCount;
		hero.setModified();
	}

}
