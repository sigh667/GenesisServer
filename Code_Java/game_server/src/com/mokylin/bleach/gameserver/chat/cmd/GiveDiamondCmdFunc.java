package com.mokylin.bleach.gameserver.chat.cmd;

import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.gameserver.human.Human;

/**
 * 给钻石的GM命令。<p>
 * 
 * 格式：givediamond amount[正整数]
 * 
 * @author pangchong
 *
 */
public class GiveDiamondCmdFunc extends AbstractGiveMoneyCmdFunc {

	@Override
	public String getGmCmd() {
		return "givediamond";
	}


	@Override
	public void giveMoney(Human human, long diamond) {
		human.giveMoney(CurrencyPropId.FREE_DIAMOND, diamond);		
		human.sendMessage(human.buildDetailInfo());
	}

}
