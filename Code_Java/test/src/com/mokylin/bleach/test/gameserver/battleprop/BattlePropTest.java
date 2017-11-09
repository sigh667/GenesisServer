package com.mokylin.bleach.test.gameserver.battleprop;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

import com.mokylin.bleach.common.prop.battleprop.BattlePropContainer;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PEIdFactory;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;
import com.mokylin.bleach.common.prop.battleprop.source.PropSourceType;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.protobuf.HeroMessage.GCChangedProps;
import com.mokylin.bleach.protobuf.HeroMessage.PropData;

public class BattlePropTest {

	@Test
	public void all_initial_battle_values_are_zero() {
		
		PEIdFactory.init();
		
		Human human = new MockHuman();
		BattlePropContainer propContainer = new BattlePropContainer(human);
		
		for(HeroBattlePropId propId : HeroBattlePropId.values()) {
			assertThat(propContainer.getProp(propId), is(propId.amendValue(0f)));
		}
	}
	
	@Test
	public void the_value_of_the_POWER_should_be_correct_with_calculateRelatedPropContainer_method() {// calculateRelatedPropContainer方法会向客户端发送属性变更消息
		
		PEIdFactory.init();
		
		MockHuman human = new MockHuman();
		Hero hero1 = new Hero(human);
		Hero hero2 = new Hero(human);
		
		Equip equip = new Equip(1);
		
		hero1.getPropContainer().addProp(equip);
		hero1.getPropContainer().addProp(equip);
		hero2.getPropContainer().addProp(equip);
		
		equip.getProp().add(EquipPropPart.Enhence, new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Abs, 100));
		equip.getProp().calculateRelatedPropContainerAndNotify();
		
		// 检查消息是否正常发送
		assertThat(human.getMessageSendCount(), is(2));
		GCChangedProps msg = human.getMsg();
		List<PropData> propDatas = msg.getChangedPropsList();
		assertThat(propDatas.size(), is(1));
		PropData propData = propDatas.get(0);
		assertThat(propData.getPropId(), is(HeroBattlePropId.LiLiang.ordinal()));
		assertThat(propData.getValue(), is(100f));
		
		// 检查英雄的属性是否正确
		assertThat(hero1.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(100f));
		assertThat(hero2.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(100f));
		
		equip.getProp().add(EquipPropPart.Enhence, new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Per, 5000));
		equip.getProp().calculateRelatedPropContainerAndNotify();
		
		// 检查消息是否正常发送
		assertThat(human.getMessageSendCount(), is(4));
		msg = human.getMsg();
		propDatas = msg.getChangedPropsList();
		assertThat(propDatas.size(), is(1));
		propData = propDatas.get(0);
		assertThat(propData.getPropId(), is(HeroBattlePropId.LiLiang.ordinal()));
		assertThat(propData.getValue(), is(150f));
		
		// 检查英雄的属性是否正确
		assertThat(hero1.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(150f));
		assertThat(hero2.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(150f));
	}
	
	@Test
	public void the_value_of_the_POWER_should_be_correct_with_calculate_method() {// calculate方法不会向客户端发送属性变更消息
		
		PEIdFactory.init();
		
		Hero hero = new Hero(new MockHuman());
		
		Equip equip = new Equip(1);
		equip.getProp().add(EquipPropPart.Original, new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Abs, 100));
		
		hero.getPropContainer().addProp(equip);
		hero.getPropContainer().addProp(equip);
		hero.getPropContainer().calculate();
		
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(100f));
		
		Level level = new Level(1);
		level.getProp().add(LevelPropPart.Original, new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Per, 5000));
		
		hero.getPropContainer().addProp(level);
		hero.getPropContainer().calculate();
		
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(150f));
	}
	
	@Test
	public void the_value_of_the_POWER_should_be_correct() {
		
		PEIdFactory.init();
		
		Human human = new MockHuman();
		Hero hero = new Hero(human);
		
		Equip equip = new Equip(1);
		equip.getProp().add(EquipPropPart.Original, new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Abs, 100));
		
		hero.getPropContainer().addProp(equip);
		hero.getPropContainer().addProp(equip);
		hero.getPropContainer().calculateAndNotify();
		
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(100f));
		
		hero.getPropContainer().removeProp(equip);
		hero.getPropContainer().calculateAndNotify();
		// POWER的最小值是1
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(1f));
		
		Level level = new Level(1);
		level.getProp().add(LevelPropPart.Original, new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Per, 5000));
		
		hero.getPropContainer().addProp(level);
		hero.getPropContainer().calculateAndNotify();
		
		// POWER的最小值是1
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(1f));
	}
	
	@Test
	public void the_value_of_the_POWER_should_be_correct_for_specific_source_type_and_id() {
		
		PEIdFactory.init();
		
		Human human = new MockHuman();
		Hero hero = new Hero(human);
		
		Equip equip = new Equip(1);
		equip.getProp().add(EquipPropPart.Original, new BattlePropEffect(HeroBattlePropId.LiLiang, PropEffectType.Abs, 100));
		
		hero.getPropContainer().addProp(PropSourceType.EQUIP, 100, equip);
		hero.getPropContainer().addProp(PropSourceType.EQUIP, 100, equip);
		hero.getPropContainer().calculateAndNotify();
		
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(100f));
		
		hero.getPropContainer().removePropByTypeId(PropSourceType.EQUIP, 100);
		hero.getPropContainer().calculateAndNotify();
		// POWER的最小值是1
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(1f));
		
		
		hero.getPropContainer().addProp(PropSourceType.EQUIP, 100, equip);
		hero.getPropContainer().calculateAndNotify();
		
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(100f));
		
		hero.getPropContainer().removePropByType(PropSourceType.EQUIP);
		hero.getPropContainer().calculateAndNotify();
		// POWER的最小值是1
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.LiLiang), is(1f));
	}
	
	@Test
	public void the_value_of_the_ATTACK_scope_should_be_correct() {
		
		PEIdFactory.init();
		
		Human human = new MockHuman();
		Hero hero = new Hero(human);
		
		Equip equip = new Equip(1);
		equip.getProp().add(EquipPropPart.Original, new BattlePropEffect(HeroBattlePropId.GongJiLiMin, PropEffectType.Abs, 50));
		equip.getProp().add(EquipPropPart.Original, new BattlePropEffect(HeroBattlePropId.GongJiLiMax, PropEffectType.Abs, 100));
		
		hero.getPropContainer().addProp(equip);
		hero.getPropContainer().addProp(equip);
		hero.getPropContainer().calculate();;
		
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.GongJiLiMin), is(50f));
		assertThat(hero.getPropContainer().getProp(HeroBattlePropId.GongJiLiMax), is(150f));
	}
}
