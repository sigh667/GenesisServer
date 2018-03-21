package com.genesis.test.gameserver.battleprop;

import com.genesis.common.prop.battleprop.BattlePropContainer;
import com.genesis.common.prop.battleprop.propholder.IPropHolder;
import com.genesis.common.prop.battleprop.propholder.Prop;
import com.genesis.common.prop.battleprop.source.PropSourceType;

public class Level implements IPropHolder<LevelPropPart> {

    //	private final int level;
    private final Prop<LevelPropPart> propEffectManager;

    public Level(int level) {
        //		this.level = level;
        this.propEffectManager =
                new Prop<LevelPropPart>(PropSourceType.StarAndLevel, level, LevelPropPart.class);
    }

    @Override
    public Prop<LevelPropPart> getProp() {
        return this.propEffectManager;
    }

    @Override
    public void addProp(BattlePropContainer battlePropContainer) {
        // TODO Auto-generated method stub

    }

}
