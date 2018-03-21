package com.genesis.remotelogserver.funcs;

import com.genesis.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.core.msgfunc.server.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.remotelogserver.Globals;
import com.genesis.servermsg.remotelogserver.Shutdown;

/**
 * 关闭日志服务器的消息处理器
 * @author yaguang.xiao
 *
 */
public class ShutdownFunc implements IServerMsgFunc<Shutdown, MsgArgs, MsgArgs> {

    @Override
    public void handle(IRemote remote, Shutdown msg, MsgArgs arg1, MsgArgs arg2) {
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
