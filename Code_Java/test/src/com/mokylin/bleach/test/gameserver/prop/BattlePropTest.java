package com.mokylin.bleach.test.gameserver.prop;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PEIdFactory;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;
import com.mokylin.bleach.gameserver.hero.Hero;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.test.gameserver.battleprop.Equip;
import com.mokylin.bleach.test.gameserver.battleprop.EquipPropPart;
import com.mokylin.bleach.test.gameserver.battleprop.MockHuman;

public class BattlePropTest {

	
	@Test
	public void the_hero_prop_should_be_change_and_get_correctly() {
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
		
		assertThat(hero.get(552), is(50f));
		assertThat(hero.get(553), is(150f));
	}
	
//	@Test
//	public void the_human_prop_should_be_change_and_get_correctly() {
//		Human human = new MockHuman();
//		
//		human.giveMoney(CurrencyPropId.CHARGE_DIAMOND, 100);
//		human.set(HumanPropId.LEVEL, 12);
//		
//		assertThat(human.get(1), is(100L));
//		assertThat(human.get(51), is(12L));
//	}
	
}
