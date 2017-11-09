package com.mokylin.bleach.common.combat;

import com.mokylin.bleach.common.combat.template.CombatTemplate;
import com.mokylin.bleach.common.core.GlobalData;

/**
 * 战场主逻辑
 * @author baoliang.shen
 *
 */
public class CombatProcess {

	/**战场状态*/
	private CombatStatus combatStatus;
	/**攻方*/
	private CombatRoles attackRoles;
	/**守方*/
	private CombatRoles defenceRoles;
	
	/**战报记录器*/
	private CombatProtobuf combatProtobuf = new CombatProtobuf();
	
	
	/**
	 * 战斗主逻辑
	 */
	public void start() {
		//初始化
		init();
		
		/**
		 * 两层循环，LOOP:[回合]{LOOP[部队行动]{}}
		 */
		//依据战斗类型，允许的最大回合数不同
		CombatTemplate template = GlobalData.getTemplateService().get(combatStatus.getCombatType().getIndex(), CombatTemplate.class);
		while (combatStatus.getCurrentRound() < template.getMaxRound()) {
			combatStatus.increaseCurrentRound();

			processRound(combatStatus.getCurrentRound());
			if (combatStatus.isFinished())
				break;
		}
		
//		// 战斗结束，生成战果
//		makeBattleResult();
	}

	/**
	 * 处理战斗回合逻辑
	 * @param round
	 */
	private void processRound(int round) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * 战场初始化
	 */
	private void init() {
		// TODO Auto-generated method stub
		//攻方初始化
		attackRoles.init();
		//守方初始化
		defenceRoles.init();
	}

	public CombatStatus getCombatStatus() {
		return combatStatus;
	}

	public CombatProtobuf getCombatProtobuf() {
		return combatProtobuf;
	}
}
