package com.genesis.core.msgfunc.server2client;

import com.genesis.core.isc.session.ISession;
import com.genesis.core.msgfunc.MsgArgs;
import com.google.protobuf.GeneratedMessage;

public interface IServer2ClientMsgFunc<Msg extends GeneratedMessage, Args1 extends MsgArgs, Args2 extends MsgArgs> {

    public void handle(ISession session, Msg msg, Args1 arg1, Args2 arg2);
}
