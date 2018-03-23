package com.genesis.gateserver.funcs;

import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.agentserver.AcceptPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcceptPlayerFunc implements IServerMsgFunc<AcceptPlayer, MsgArgs, MsgArgs> {

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
