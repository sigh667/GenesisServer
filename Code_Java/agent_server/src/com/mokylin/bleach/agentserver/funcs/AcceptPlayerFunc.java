package com.mokylin.bleach.agentserver.funcs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.agentserver.AcceptPlayer;

public class AcceptPlayerFunc implements IServerMsgFunc<AcceptPlayer, MsgArgs, MsgArgs>{
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(AcceptPlayerFunc.class);

	@Override
	public void handle(IRemote remote, AcceptPlayer msg, MsgArgs arg1, MsgArgs arg2) {
		// TODO Auto-generated method stub
		String string = msg.accountId + msg.channel;
		string = string + msg.humanUuid;
		
		logger.debug(string + "===================");
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}

}
