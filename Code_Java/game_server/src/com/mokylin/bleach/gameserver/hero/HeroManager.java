package com.mokylin.bleach.gameserver.hero;

import java.util.HashMap;
import java.util.List;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.hero.template.HeroAttrTemplate;
import com.mokylin.bleach.common.hero.template.HeroGroupTemplate;
import com.mokylin.bleach.gamedb.orm.entity.HeroEntity;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HeroMessage.GCAllHeroInfo;
import com.mokylin.bleach.protobuf.HeroMessage.GCAllHeroInfo.Builder;
import com.mokylin.bleach.protobuf.HeroMessage.GCHeroAdd;
import com.mokylin.bleach.protobuf.HeroMessage.GCHeroUpdate;
import com.mokylin.bleach.protobuf.HeroMessage.HeroInfo;

/**
 * 玩家身上的英雄管理器
 * @author baoliang.shen
 *
 */
public class HeroManager {

	private Human human;
	/**<UUID,Hero>所有英雄*/
	private HashMap<Long, Hero> heroMap = new HashMap<>();

	public HeroManager(Human human) {
		this.human = human;
	}

	public void loadFromEntity(List<HeroEntity> heroEntityList) {
		if (heroEntityList==null)
			return;

		for (HeroEntity heroEntity : heroEntityList) {
			Hero hero = Hero.buildFromHeroEntity(heroEntity, human);
			heroMap.put(hero.getDbId(), hero);
		}

		for (Hero hero : heroMap.values()) {
			hero.getPropContainer().calculate();
		}
	}

	/**
	 * 登录时，给客户端发英雄所有信息
	 */
	public void notifyOnLogin() {
		Builder allHeroBuilder = GCAllHeroInfo.newBuilder();
		com.mokylin.bleach.protobuf.HeroMessage.HeroInfo.Builder heroBuilder = HeroInfo.newBuilder();
		for (Hero hero : heroMap.values()) { 
			final HeroInfo heroInfo = hero.builderHeroInfo(heroBuilder);
			allHeroBuilder.addHeroInfos(heroInfo);
		}
		GCAllHeroInfo msg = allHeroBuilder.build();
		human.sendMessage(msg);
	}

	public void notifyHeroAdd(Hero hero) {
		HeroInfo.Builder builder = HeroInfo.newBuilder();
		HeroInfo heroInfo = hero.builderHeroInfo(builder);
		GCHeroAdd msg = GCHeroAdd.newBuilder().setHeroAdd(heroInfo).build();
		
		this.human.sendMessage(msg);
	}

	public void notifyHeroUpdate(Hero hero) {
		HeroInfo.Builder builder = HeroInfo.newBuilder();
		HeroInfo heroInfo = hero.builderHeroInfo(builder);
		GCHeroUpdate msg = GCHeroUpdate.newBuilder().setHeroInfo(heroInfo).build();
		
		this.human.sendMessage(msg);
	}

	/**
	 * 单纯的新添加一个Hero
	 * @param heroGroupId	英雄组ID
	 * @return 
	 */
	public Hero addHero(int heroGroupId) {
		return addHero(Globals.getHeroGroupService().get(heroGroupId));
	}
	
	/**
	 * 单纯的根据Hero的template id添加一个Hero
	 * @param templateId
	 * @return
	 */
	public Hero addHeroByTemplateId(int templateId){
		return addHero(GlobalData.getTemplateService().get(templateId, HeroAttrTemplate.class));
	}
	
	private Hero addHero(HeroAttrTemplate template){
		Hero hero = Hero.buildNewHeroByTemplate(template, human);
		heroMap.put(hero.getDbId(), hero);
		return hero;
	}

	public Hero getHero(long heroUuid) {
		return heroMap.get(heroUuid);
	}

	/**
	 * 给予新进入的玩家初始英雄
	 */
	public void giveInitialHeros() {
		//黑崎一护
		Hero hero = this.addHero(1);
		//茶渡泰虎
		this.addHero(2);
		//井上织姬
		this.addHero(4);
		
		//给黑崎一护带两个装备
		hero.getEquipManager().wear(0);
		hero.getEquipManager().wear(2);
	}

	/**
	 * 取英雄组ID对应的道具ID
	 * @param heroGroupId	Hero组ID
	 * @return
	 */
	public static int getItemId(int heroGroupId) {
		final HeroGroupTemplate heroGroupTemplate = GlobalData.getTemplateService().get(heroGroupId, HeroGroupTemplate.class);
		return heroGroupTemplate.getItemId();
	}
}
