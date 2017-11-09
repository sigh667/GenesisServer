package com.mokylin.bleach.servermsg.gameserver;

import com.mokylin.bleach.core.isc.msg.IMessage;
import com.mokylin.bleach.gamedb.human.HumanData;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class HumanDataMsg implements IMessage{
	
	/** 超时ID */
	public final long timeoutId;
	/**玩家数据*/
	public final HumanData humanData;
	/**账号ID*/
	public final String accountId;
	/**渠道*/
	public final String channel;
	
	public HumanDataMsg(long timeoutId, HumanData humanData, String accountId, String channel){
		this.timeoutId = timeoutId;
		this.humanData = humanData;
		this.accountId = accountId;
		this.channel = channel;
	}
	
	@Override
	public MessageTarget getTarget() {
		return MessageTarget.PLAYER_MANAGER;
	}
}
