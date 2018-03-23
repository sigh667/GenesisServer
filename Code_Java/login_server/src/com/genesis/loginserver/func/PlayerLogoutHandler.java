package com.genesis.loginserver.func;

import com.genesis.servermsg.core.isc.remote.IRemote;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.servermsg.core.msgfunc.IServerMsgFunc;
import com.genesis.protobuf.MessageType.MessageTarget;
import com.genesis.servermsg.agentserver.AcceptPlayer;
import com.genesis.servermsg.loginserver.PlayerLogout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerLogoutHandler implements IServerMsgFunc<PlayerLogout, MsgArgs, MsgArgs> {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(PlayerLogoutHandler.class);

    @Override
    public void handle(IRemote remote, PlayerLogout msg, MsgArgs arg1, MsgArgs arg2) {
        //干点啥事呢，比如把全局计数器减少 TODO
        int a = 0;
        a++;
        a++;
        logger.debug("=======================" + msg.accountId + msg.channel + a);

        AcceptPlayer acceptPlayer = new AcceptPlayer("账号1", "TX", 999, "qwe");
        remote.sendMessage(acceptPlayer);

        //		IRemote iRemote = Globals.getIscService().getRemote(ServerType.GATE, 1).get();
        //		iRemote.sendMessage(acceptPlayer);
    }

    @Override
    public MessageTarget getTarget() {
        return MessageTarget.ISC_ACTOR;
    }

}
