package com.mokylin.bleach.test.gameserver.battleprop;

import java.util.List;

import com.google.common.collect.Lists;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;

public class ListPerformanceTest {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		for(int i = 0;i < 100000;i ++) {
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
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println(endTime - startTime);
	}
}
