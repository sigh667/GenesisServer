package com.genesis.test.gameserver.prop;

import com.genesis.common.core.excelmodel.TempAttrNode3Col;
import com.genesis.common.prop.battleprop.HeroBattlePropId;
import com.genesis.common.prop.battleprop.propeffect.BattlePropEffect;
import com.genesis.common.prop.battleprop.propeffect.PropEffectConverter;
import com.genesis.common.prop.battleprop.propeffect.PropEffectType;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
