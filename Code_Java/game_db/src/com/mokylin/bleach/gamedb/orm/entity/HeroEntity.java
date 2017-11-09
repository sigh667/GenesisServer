package com.mokylin.bleach.gamedb.orm.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import com.mokylin.bleach.gamedb.orm.EntityWithRedisKey;
import com.mokylin.bleach.gamedb.orm.IHumanRelatedEntity;
import com.mokylin.bleach.gamedb.orm.vo.HeroEquip;
import com.mokylin.bleach.gamedb.orm.vo.HeroSkill;
import com.mokylin.bleach.gamedb.redis.key.model.HeroKey;

@Entity
@Table(name = "t_hero")
public class HeroEntity implements EntityWithRedisKey<HeroKey>, IHumanRelatedEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**UUID*/
	private Long id;
	/**所属角色ID*/
	private long humanId;
	/**模板ID*/
	private int templateId;
	/**级别*/
	private int level;
	/**星星数*/
	private int starCount;
	/**英雄碎片数量*/
	private int fragmentCount;
	/** 英雄技能列表（本数组第一个元素为斩魄刀技，其余元素为辅助技） */
	private HeroSkill[] heroSkills;
	/** 英雄装备列表 */
	private HeroEquip[] heroEquips;
	/**创建时间*/
	private Timestamp createTime;
	
	@Id
	@Column
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long humanId() {
		return humanId;
	}

	@Column
	public long getHumanId() {
		return humanId;
	}
	
	public void setHumanId(long humanId) {
		this.humanId = humanId;
	}
	
	@Column
	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	
	@Column
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public HeroKey newRedisKey(Integer serverId) {
		return new HeroKey(serverId, this.humanId(), this.getId());
	}

	@Column
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Column
	public int getFragmentCount() {
		return fragmentCount;
	}

	public void setFragmentCount(int fragmentCount) {
		this.fragmentCount = fragmentCount;
	}

	@Type(type = "com.mokylin.bleach.gamedb.orm.type.HeroSkillType")
	@Columns(columns={
			@Column(name="skillTemplateId1"),
			@Column(name="skillLevel1"),
			@Column(name="skillTemplateId2"),
			@Column(name="skillLevel2"),
			@Column(name="skillTemplateId3"),
			@Column(name="skillLevel3"),
			@Column(name="skillTemplateId4"),
			@Column(name="skillLevel4"),
			@Column(name="skillTemplateId5"),
			@Column(name="skillLevel5"),
			@Column(name="skillTemplateId6"),
			@Column(name="skillLevel6")
	})
	public HeroSkill[] getHeroSkills() {
		return heroSkills;
	}

	public void setHeroSkills(HeroSkill[] heroSkills) {
		this.heroSkills = heroSkills;
	}

	@Type(type = "com.mokylin.bleach.gamedb.orm.type.HeroEquipType")
	@Columns(columns={
			@Column(name="equipTemplateId1"),
			@Column(name="equipEnchantLevel1"),
			@Column(name="equipEnchantExp1"),
			@Column(name="equipTemplateId2"),
			@Column(name="equipEnchantLevel2"),
			@Column(name="equipEnchantExp2"),
			@Column(name="equipTemplateId3"),
			@Column(name="equipEnchantLevel3"),
			@Column(name="equipEnchantExp3"),
			@Column(name="equipTemplateId4"),
			@Column(name="equipEnchantLevel4"),
			@Column(name="equipEnchantExp4"),
			@Column(name="equipTemplateId5"),
			@Column(name="equipEnchantLevel5"),
			@Column(name="equipEnchantExp5"),
			@Column(name="equipTemplateId6"),
			@Column(name="equipEnchantLevel6"),
			@Column(name="equipEnchantExp6")
	})
	public HeroEquip[] getHeroEquips() {
		return heroEquips;
	}

	public void setHeroEquips(HeroEquip[] heroEquips) {
		this.heroEquips = heroEquips;
	}

	@Column
	public int getStarCount() {
		return starCount;
	}

	public void setStarCount(int starCount) {
		this.starCount = starCount;
	}

}
