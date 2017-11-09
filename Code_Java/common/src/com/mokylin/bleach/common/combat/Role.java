package com.mokylin.bleach.common.combat;

import com.mokylin.bleach.common.prop.IPropNotifier;
import com.mokylin.bleach.common.prop.battleprop.BattlePropContainer;

/**
 * 战斗单位
 * @author baoliang.shen
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
