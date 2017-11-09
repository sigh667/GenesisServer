package com.mokylin.bleach.gameserver.human.energy;


import java.util.Map;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.human.template.EnergyTemplate;

/**
 * 玩家体力Helper
 * 
 * @author xiao.chang
 */
public class EnergyHelper{
	
	/**
	 * Get等级对应的体力上限
	 * 
	 * @param level 等级
	 * 
	 * @return 该等级的体力上限
	 * @throws IllegalArgumentException 参数不合法
	 */
	public static int getMaxEnergyByLevel(long level){
		Map<Integer, EnergyTemplate> energyMap = GlobalData.getTemplateService().getAll(EnergyTemplate.class);
		if (level <= 0 || level > energyMap.size()) {
			throw new IllegalArgumentException(
					String.format("level=%d (level必须>0, 且<=已定义的最高等级 %d)",level,energyMap.size()));
		}
		
		return energyMap.get((int)level).getEnergy();
	}
	
}
