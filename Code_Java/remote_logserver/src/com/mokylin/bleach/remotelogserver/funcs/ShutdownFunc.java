package com.mokylin.bleach.remotelogserver.funcs;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.remotelogserver.Globals;
import com.mokylin.bleach.servermsg.remotelogserver.Shutdown;

/**
 * 关闭日志服务器的消息处理器
 * @author yaguang.xiao
 *
 */
public class ShutdownFunc implements IServerMsgFunc<Shutdown, MsgArgs, MsgArgs> {

	@Override
	public void handle(IRemote remote, Shutdown msg, MsgArgs arg1,
			MsgArgs arg2) {
		Globals.getMainthread().execute(new Runnable() {

			@Override
			public void run() {
				Globals.shutdown();
			}
			
		});
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.ISC_ACTOR;
	}

}
