package com.genesis.common.combat;

import com.genesis.common.prop.IPropNotifier;
import com.genesis.common.prop.battleprop.BattlePropContainer;

/**
 * 战斗单位
 * @author Joey
 *
 */
public class Role {

    /** 战斗属性容器 */
    private final BattlePropContainer battlePropContainer;


    public Role(IPropNotifier iPropNotifier) {
        this.battlePropContainer = new BattlePropContainer(iPropNotifier);
    }

    public BattlePropContainer getPropContainer() {
        return this.battlePropContainer;
    }
}
