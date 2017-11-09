package com.mokylin.bleach.core.msgfunc.server;

import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public interface IServerMsgFunc<T, Arg1 extends MsgArgs, Arg2 extends MsgArgs> {

	public void handle(IRemote remote, T msg, Arg1 arg1, Arg2 arg2);
	
	/**
	 * 获取该处理器执行的Target Actor
	 * @return
	 */
	public MessageTarget getTarget();
}
