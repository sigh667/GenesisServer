package com.mokylin.bleach.common.prop.battleprop.propholder;

import java.util.Iterator;
import java.util.List;

import com.mokylin.bleach.common.prop.battleprop.propeffect.BattlePropEffect;
import com.mokylin.bleach.common.prop.battleprop.propeffect.FinalBattleProp;

/**
 * 属性作用工具
 * @author yaguang.xiao
 *
 */
public class PropEffectUtil {

	/**
	 * 添加属性修改到属性作用列表中
	 * @param change
	 * @param propEffects
	 */
	public static void addPropEffect(BattlePropEffect change, List<FinalBattleProp> propEffects) {
		if(change == null || !change.isValid()) {
			return;
		}
		
		boolean find = false;
		
		Iterator<FinalBattleProp> propsIt = propEffects.iterator();
		while(propsIt.hasNext()) {
			FinalBattleProp prop = propsIt.next();
			if(change.isSameType(prop)) {
				find = true;
				prop.changeValue(change.getValue());
				if(prop.getValue() == 0) {
					propsIt.remove();
				}
				
				break;
			}
		}
		
		if(!find) {
			propEffects.add(change.cloneFinalBattleProp());
		}
	}
}
