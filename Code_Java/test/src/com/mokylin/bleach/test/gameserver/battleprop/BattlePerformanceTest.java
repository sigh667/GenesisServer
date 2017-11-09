package com.mokylin.bleach.test.gameserver.battleprop;

import java.util.List;

import com.google.common.collect.Lists;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PEIdFactory;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;
import com.mokylin.bleach.common.prop.battleprop.propholder.Prop;
import com.mokylin.bleach.gameserver.hero.Hero;

public class BattlePerformanceTest {

	private static long equipId = 0;
	
	public static void main(String[] args) {
		PEIdFactory.init();
		
		List<MockHuman> humanList = Lists.newLinkedList();
		
		for(int i = 0;i < 1000;i ++) {
			humanList.add(createHuman());
		}
		
		System.out.println("start!!!");
		long startTime = System.currentTimeMillis();
		for(MockHuman human : humanList) {
			
			List<BattlePropEffect> effects = Lists.newLinkedList();
			effects.add(new BattlePropEffect(HeroBattlePropId.GongJiLiMax, PropEffectType.Abs, 200));
			effects.add(new BattlePropEffect(HeroBattlePropId.GongJiLiMax, PropEffectType.Per, 10000));
			effects.add(new BattlePropEffect(HeroBattlePropId.GongJiLiMin, PropEffectType.Abs, 200));
			effects.add(new BattlePropEffect(HeroBattlePropId.GongJiLiMin, PropEffectType.Per, 10000));
			effects.add(new BattlePropEffect(HeroBattlePropId.MaxHP, PropEffectType.Abs, 200));
			effects.add(new BattlePropEffect(HeroBattlePropId.MaxHP, PropEffectType.Per, 10000));
			effects.add(new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Abs, 200));
			effects.add(new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Per, 10000));
			effects.add(new BattlePropEffect(HeroBattlePropId.Lingli, PropEffectType.Abs, 200));
			effects.add(new BattlePropEffect(HeroBattlePropId.Lingli, PropEffectType.Per, 10000));
			
			Prop<EquipPropPart> prop = human.getAllHerosEffect().getProp();
			prop.resetPropEffect(EquipPropPart.Enhence, effects);
			prop.calculateRelatedPropContainerAndNotify();
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println(endTime - startTime);
	}
	
	/**
	 * 创建Human
	 * @return
	 */
	private static MockHuman createHuman() {
		MockHuman human = new MockHuman();
		
		for(int i = 0;i < 5;i ++) {
			createHero(human);
		}
		
		human.addAllHeroEffect(createEquip());
		
		human.calculate();
		return human;
	}
	
	/**
	 * 给玩家添加一个英雄
	 * @param human
	 * @return
	 */
	private static Hero createHero(MockHuman human) {
		Hero hero = new Hero(human);
		human.addHero(hero);
		
		for(int i = 0;i < 20;i ++) {
			hero.getPropContainer().addProp(createEquip());
		}
		
		return hero;
	}
	
	/**
	 * 创建装备对象
	 * @return
	 */
	private static Equip createEquip() {
		Equip equip = new Equip(++ equipId);
//		equip.getProp().change(EquipPropPart.Original, HeroBattlePropId.ATTACK_UPPER_BOUND, PropEffectType.Abs, 100);
//		equip.getProp().change(EquipPropPart.Original, HeroBattlePropId.ATTACK_UPPER_BOUND, PropEffectType.Per, 5000);
//		equip.getProp().change(EquipPropPart.Original, HeroBattlePropId.ATTACK_LOWER_BOUND, PropEffectType.Abs, 100);
//		equip.getProp().change(EquipPropPart.Original, HeroBattlePropId.ATTACK_LOWER_BOUND, PropEffectType.Per, 5000);
//		equip.getProp().change(EquipPropPart.Original, HeroBattlePropId.HP, PropEffectType.Abs, 100);
//		equip.getProp().change(EquipPropPart.Enhence, HeroBattlePropId.HP, PropEffectType.Per, 5000);
//		equip.getProp().change(EquipPropPart.Enhence, HeroBattlePropId.ZHAN, PropEffectType.Abs, 100);
//		equip.getProp().change(EquipPropPart.Enhence, HeroBattlePropId.ZHAN, PropEffectType.Per, 5000);
//		equip.getProp().change(EquipPropPart.Enhence, HeroBattlePropId.JI, PropEffectType.Abs, 100);
//		equip.getProp().change(EquipPropPart.Enhence, HeroBattlePropId.JI, PropEffectType.Per, 5000);
		return equip;
	}
}
