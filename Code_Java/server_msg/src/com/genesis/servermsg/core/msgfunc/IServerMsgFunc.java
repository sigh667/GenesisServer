package com.genesis.servermsg.core.msgfunc;

import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.protobuf.MessageType.MessageTarget;

public interface IServerMsgFunc<T, Arg1 extends MsgArgs, Arg2 extends MsgArgs> {

    public void handle(IRemote remote, T msg, Arg1 arg1, Arg2 arg2);

    /**
     * 获取该处理器执行的Target Actor
     * @return
     */
    public MessageTarget getTarget();
}
