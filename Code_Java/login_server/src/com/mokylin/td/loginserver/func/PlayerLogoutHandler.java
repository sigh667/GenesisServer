package com.mokylin.td.loginserver.func;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.agentserver.AcceptPlayer;
import com.mokylin.bleach.servermsg.loginserver.PlayerLogout;

public class PlayerLogoutHandler implements IServerMsgFunc<PlayerLogout, MsgArgs, MsgArgs> {
	
	/** 日志 */
	private static final Logger logger = LoggerFactory.getLogger(PlayerLogoutHandler.class);

	@Override
	public void handle(IRemote remote, PlayerLogout msg, MsgArgs arg1, MsgArgs arg2) {
		//干点啥事呢，比如把全局计数器减少 TODO
		int a =0;
		a++;
		a++;
		logger.debug("======================="+ msg.accountId + msg.channel + a);
		
		AcceptPlayer acceptPlayer = new AcceptPlayer("账号1", "TX", 999, "qwe");
		remote.sendMessage(acceptPlayer);
		
//		IRemote iRemote = com.mokylin.td.loginserver.globals.Globals.getIscService().getRemote(ServerType.AGENT_SERVER, 1).get();
//		iRemote.sendMessage(acceptPlayer);
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}

}
