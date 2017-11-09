package com.mokylin.bleach.core.msgfunc.server2client;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.core.isc.session.ISession;
import com.mokylin.bleach.core.msgfunc.MsgArgs;

public interface IServer2ClientMsgFunc<Msg extends GeneratedMessage, Args1 extends MsgArgs, Args2 extends MsgArgs> {

	public void handle(ISession session, Msg msg, Args1 arg1, Args2 arg2);
}
