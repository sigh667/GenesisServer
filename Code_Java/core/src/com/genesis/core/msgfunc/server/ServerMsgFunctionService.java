package com.genesis.core.msgfunc.server;

import com.genesis.core.msgfunc.MsgArgs;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import com.genesis.core.isc.remote.IRemote;
import com.genesis.protobuf.MessageType.MessageTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ServerMsgFunctionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ImmutableTable<MessageTarget, Class<?>, IServerMsgFunc<?, MsgArgs, MsgArgs>>
            funcTable;

    public ServerMsgFunctionService(
            Table<MessageTarget, Class<?>, IServerMsgFunc<?, MsgArgs, MsgArgs>> funcs) {
        funcTable = ImmutableTable
                .copyOf(checkNotNull(funcs, "MsgFunctionService can not init with null funcs!"));
    }

    @SuppressWarnings("unchecked")
    public <T, Args1 extends MsgArgs, Args2 extends MsgArgs> void handle(MessageTarget msgTarget,
            IRemote remote, T msg, Args1 arg1, Args2 arg2) {
        IServerMsgFunc<T, MsgArgs, MsgArgs> func =
                (IServerMsgFunc<T, MsgArgs, MsgArgs>) this.funcTable.get(msgTarget, msg.getClass());
        if (func != null) {
            func.handle(remote, msg, arg1, arg2);
        } else {
            log.warn("Server Message [{}] can not find handlers!", msg.getClass().getName());
        }
    }
}
