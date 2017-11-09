package com.mokylin.bleach.gameserver.chat.cmd;

import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 给金币的GM命令。<p>
 * 
 * 格式：givegold amount[正整数]
 * 
 * @author pangchong
 *
 */
public class GiveGoldCmdFunc extends AbstractGiveMoneyCmdFunc {

	@Override
	public String getGmCmd() {
		return "givegold";
	}


	@Override
	public void giveMoney(Human human, long gold) {
		human.giveMoney(CurrencyPropId.GOLD, gold);		
		human.sendMessage(human.buildDetailInfo());
	}

}
