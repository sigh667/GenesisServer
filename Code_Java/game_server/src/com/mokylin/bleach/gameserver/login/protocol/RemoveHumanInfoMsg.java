package com.mokylin.bleach.gameserver.login.protocol;

import com.mokylin.bleach.gamedb.human.HumanInfo;

public class RemoveHumanInfoMsg {
	
	private HumanInfo humanInfo;

	public RemoveHumanInfoMsg(HumanInfo humanInfo) {
		this.humanInfo = humanInfo;
	}

	public HumanInfo getHumanInfo() {
		return humanInfo;
	}
}
