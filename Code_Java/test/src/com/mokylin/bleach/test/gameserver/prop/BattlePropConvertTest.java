package com.mokylin.bleach.test.gameserver.prop;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;

import com.mokylin.bleach.common.core.excelmodel.TempAttrNode3Col;
import com.mokylin.bleach.common.prop.battleprop.HeroBattlePropId;
import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectConverter;
import com.mokylin.bleach.common.prop.battleprop.propeffect.PropEffectType;

public class BattlePropConvertTest {

	@Test
	public void the_convert_process_should_be_correct() {
		TempAttrNode3Col node = new TempAttrNode3Col();
		node.setAttributeIndex(HeroBattlePropId.BaoJi);
		node.setAbsValue(90);
		node.setPerValue(10);
		
		List<BattlePropEffect> list = PropEffectConverter.Inst.convertToBattlePropEffect(node);
		
		{
			BattlePropEffect battlePE = list.get(0);
			assertThat(battlePE.getPropId(), is(HeroBattlePropId.LiLiang));
			assertThat(battlePE.getType(), is(PropEffectType.Abs));
			assertThat(battlePE.getValue(), is(90));
		}

		{
			BattlePropEffect battlePE = list.get(1);
			assertThat(battlePE.getPropId(), is(HeroBattlePropId.LiLiang));
			assertThat(battlePE.getType(), is(PropEffectType.Per));
			assertThat(battlePE.getValue(), is(10));
		}
	}
}
