package com.mokylin.bleach.common.combat;

public class CombatStatus {

	/**战斗类型*/
	private CombatType combatType;
	/**当前回合数*/
	private int currentRound;
	/**战斗结束标记*/
	private boolean isFinished;


	public CombatStatus(CombatType combatType, int currentRound, boolean isFinished) {
		this.combatType = combatType;
		this.currentRound = currentRound;
		this.isFinished = isFinished;
	}

	public CombatType getCombatType() {
		return combatType;
	}

	public int getCurrentRound() {
		return currentRound;
	}
	public void increaseCurrentRound() {
		currentRound += 1;
	}

	public boolean isFinished() {
		return isFinished;
	}
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
}
