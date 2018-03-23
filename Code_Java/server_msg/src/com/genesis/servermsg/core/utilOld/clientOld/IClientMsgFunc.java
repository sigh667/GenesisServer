package com.genesis.servermsg.core.utilOld.clientOld;

import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.servermsg.core.isc.session.ISession;
import com.google.protobuf.GeneratedMessage;

public interface IClientMsgFunc<Msg extends GeneratedMessage, Args1 extends MsgArgs, Args2 extends MsgArgs> {

    public void handle(ISession session, Msg msg, Args1 arg1, Args2 arg2);
}
