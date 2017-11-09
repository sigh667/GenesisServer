package com.mokylin.bleach.gameserver.hero;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Timestamp;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.exp.ExpData;
import com.mokylin.bleach.common.hero.template.HeroAttrTemplate;
import com.mokylin.bleach.common.hero.template.HeroGroupTemplate;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.common.human.template.ExpTemplate;
import com.mokylin.bleach.common.prop.battleprop.BattlePropContainer;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.core.util.arr.EnumArray;
import com.mokylin.bleach.gamedb.orm.entity.HeroEntity;
import com.mokylin.bleach.gamedb.orm.vo.HeroEquip;
import com.mokylin.bleach.gamedb.orm.vo.HeroSkill;
import com.mokylin.bleach.gamedb.uuid.UUIDType;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.persistance.ObjectInSqlImpl;
import com.mokylin.bleach.gameserver.hero.equip.EquipManager;
import com.mokylin.bleach.gameserver.hero.quality.QualityProp;
import com.mokylin.bleach.gameserver.hero.skill.SkillManager;
import com.mokylin.bleach.gameserver.hero.star.StarAndLevelProp;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HeroMessage.HeroInfo;
import com.mokylin.bleach.protobuf.HeroMessage.HeroSkillInfo;

/**
 * 玩家的英雄，这里英雄的定义是可以进行战斗的单位
 * <P>
 * 每一个英雄就是一个{@code Hero}对象
 * @author yaguang.xiao
 *
 */
public class Hero extends ObjectInSqlImpl<Long, HeroEntity>{

	/**UUID*/
	private Long id;
	/**模板*/
	private HeroAttrTemplate template;
	/**级别*/
	private int level;
	/**英雄碎片数量,这个字段即将废弃 TODO*/
	private int fragmentCount;
	/**创建时间*/
	private Timestamp createTime;
	/**星星数 */
	private int starCount;

	/** 非战斗属性管理器 */
	private final EnumArray<HeroPropId, Long> prop = EnumArray.create(HeroPropId.class, Long.class, 0L);
	/** 战斗属性容器 */
	private final BattlePropContainer battlePropContainer;
	/** 角色对象 */
	private final Human owner;

	/**技能管理器*/
	private final SkillManager skillManager;
	/**装备管理器*/
	private final EquipManager equipManager;
	/**品质带来的属性加成*/
	private QualityProp qualityProp;
	/**来自星级和级别的属性加成*/
	private StarAndLevelProp starAndLevelProp;


	public Hero(Human human) {
		super(human.getDataUpdater());
		this.owner = human;
		this.battlePropContainer = new BattlePropContainer(human);
		this.skillManager = new SkillManager(human,this);
		this.equipManager = new EquipManager(human,this);
	}
	
	/**
	 * 以Entity为源头，初始化一个Hero
	 * @param entity
	 * @param owner
	 * @return
	 */
	public static Hero buildFromHeroEntity(HeroEntity entity, Human owner) {
		final int templateId = entity.getTemplateId();
		HeroAttrTemplate template = GlobalData.getTemplateService().get(templateId,
				HeroAttrTemplate.class);
		checkNotNull(template);

		Hero hero = new Hero(owner);
		hero.init(template);
		hero.fromEntity(entity);
		hero.initQualityProp();
		hero.initStarAndLevelProp();
		return hero;
	}
	
	/**
	 * 新创建一个Hero
	 * @param template
	 * @param owner
	 * @return
	 */
	public static Hero buildNewHeroByTemplate(HeroAttrTemplate template, Human owner){
		checkNotNull(template);
		Hero hero = new Hero(owner);
		hero.init(template);
		hero.giveDefaultValueOnBorn();
		hero.initQualityProp();
		hero.initStarAndLevelProp();
		
		hero.setModified();

		return hero;
	}

	private void initStarAndLevelProp() {
		starAndLevelProp = new StarAndLevelProp(this);
		starAndLevelProp.addProp(getPropContainer());
	}

	private void init(HeroAttrTemplate heroAttrTemplate) {
		this.template = heroAttrTemplate;
	}

	private void initQualityProp() {
		qualityProp = new QualityProp(this);
		qualityProp.addProp(this.getPropContainer());
	}
	private void giveDefaultValueOnBorn() {
		final int heroGroupId = this.template.getHeroGroupId();
		final HeroGroupTemplate heroGroupTemplate = GlobalData.getTemplateService().get(heroGroupId, HeroGroupTemplate.class);
		this.starCount = heroGroupTemplate.getBaseStar();

		id = this.getOwner().getServerGlobals().getUUIDGenerator().getNextUUID(UUIDType.Item);
		level = 1;
		fragmentCount = 0;
		createTime = new Timestamp(Globals.getTimeService().now());

		//给初始技能
		skillManager.giveInitialSkills();
	}

	/**
	 * 获取英雄的属性容器
	 * @return
	 */
	public BattlePropContainer getPropContainer() {
		return this.battlePropContainer;
	}

	@Override
	public Long getDbId() {
		return id;
	}

	@Override
	public HeroEntity toEntity() {
		HeroEntity heroEntity = new HeroEntity();
		heroEntity.setId(id);
		heroEntity.setLevel(level);
		heroEntity.setStarCount(starCount);
		heroEntity.setFragmentCount(fragmentCount);
		heroEntity.setCreateTime(createTime);
		heroEntity.setHumanId(this.owner.getDbId());
		heroEntity.setTemplateId(this.getTemplateId());

		//装备
		HeroEquip[] heroEquips = equipManager.buildHeroEquips();
		heroEntity.setHeroEquips(heroEquips);

		//技能
		HeroSkill[] heroSkills = skillManager.buildHeroSkills();
		heroEntity.setHeroSkills(heroSkills);

		return heroEntity;
	}

	@Override
	public void fromEntity(HeroEntity entity) {
		id = entity.getId();
		level = entity.getLevel();
		starCount = entity.getStarCount();
		fragmentCount = entity.getFragmentCount();
		createTime = entity.getCreateTime();

		//装备
		final HeroEquip[] heroEquips = entity.getHeroEquips();
		equipManager.initFromHeroEquips(heroEquips);

		//技能
		final HeroSkill[] heroSkills = entity.getHeroSkills();
		skillManager.initFromHeroSkills(heroSkills);
	}

	/**
	 * 模板ID
	 * 
	 * @return 如果已经绑定了模板，返回模板id，否则返回-1
	 */
	public int getTemplateId() {
		return template != null ? template.getId() : -1;
	}
	public HeroAttrTemplate getTemplate() {
		return this.template;
	}


	public Human getOwner() {
		return owner;
	}

	public int getFragmentCount() {
		return fragmentCount;
	}

	public int getLevel() {
		return this.level;
	}

	public HeroInfo builderHeroInfo(HeroInfo.Builder heroBuilder) {
		heroBuilder.clear();
		heroBuilder.setFragmentCount(getFragmentCount());
		heroBuilder.setId(getDbId());
		heroBuilder.setLevel(getLevel());
		heroBuilder.setTemplateId(getTemplateId());
		heroBuilder.setStarCount(this.getStarCount());

		//所有装备
		heroBuilder.addAllHeroEquipInfos(equipManager.buildEquipArrayInfos());

		/**所有技能*/
		//斩魄刀技
		HeroSkillInfo heroSkillInfo = skillManager.getSpecialSkill().buildHeroSkillInfo();
		heroBuilder.addHeroSkillInfos(heroSkillInfo);

		//辅助技
		heroBuilder.addAllHeroSkillInfos(skillManager.buildSkillArrayInfos());

		return heroBuilder.build();
	}

	/**
	 * 根据属性id数字获取对应的属性值
	 * 不需要change方法，因为只能直接改变HeroPropId属性，我们可以有更加安全的方法来做这个事情
	 * @param idIndex
	 * @return
	 */
	public float get(int idIndex) {
		if(HeroPropId.containsIndex(idIndex)) {
			return this.prop.get(HeroPropId.get(idIndex));
		} else if (HeroBattlePropId.containsIndex(idIndex)) {
			return this.battlePropContainer.getProp(HeroBattlePropId.get(idIndex));
		} else {
			throw new RuntimeException(idIndex + " is not valid for Hero or HeroBattle property!");
		}
	}

	public int getStarCount() {
		return starCount;
	}

	public SkillManager getSkillManager() {
		return skillManager;
	}

	public EquipManager getEquipManager() {
		return equipManager;
	}

	/**
	 * 升品质
	 */
	public void qualityUp() {
		if (!template.hasNextQualityHero())
			return;

		//1.0清除所有装备及装备的属性加成
		equipManager.removeAllEquip();
		
		//2.0换模板
		final int nextId = this.template.getNextHeroId();
		final HeroAttrTemplate nextTemplate = GlobalData.getTemplateService().get(nextId, HeroAttrTemplate.class);
		this.init(nextTemplate);
		qualityProp.onQualityUp(this);
		
		//3.0重置技能
		skillManager.resetAllSkill();
		
		//保存数据
		setModified();
	}

	/**
	 * 升星
	 */
	public void starUp() {
		if (!Globals.getHeroStarService().isHasNextStar(this.starCount))
			return;
		
		this.starCount++;
		starAndLevelProp.onStarUp(this);
	}
	
	/**
	 * 增加经验
	 * 
	 * @param exp 增加的经验值
	 */
	public void addExp(long exp) {
		ExpData expData = Globals.getExpService().addExp(
				prop.get(HeroPropId.LEVEL), owner.get(HumanPropId.LEVEL),
				prop.get(HeroPropId.EXP), exp, ExpTemplate.ExpEnum.HERO);
		prop.set(HeroPropId.LEVEL, (long) expData.getLevel());
		prop.set(HeroPropId.EXP, expData.getExp());
	}
	
	public boolean isExpAddable() {
		return Globals.getExpService().isAddable(prop.get(HeroPropId.LEVEL),
				owner.get(HumanPropId.LEVEL), prop.get(HeroPropId.EXP),
				ExpTemplate.ExpEnum.HERO);
	}
	
}
