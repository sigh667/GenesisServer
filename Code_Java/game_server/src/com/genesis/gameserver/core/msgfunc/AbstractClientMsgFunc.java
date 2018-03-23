package com.genesis.gameserver.core.msgfunc;

import com.google.protobuf.GeneratedMessage;

import com.genesis.servermsg.core.isc.session.ISession;
import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.servermsg.core.utilOld.clientOld.IClientMsgFunc;
import com.genesis.gameserver.player.Player;

/**
 * 客户端消息处理用的超类。<p>
 *
 * GameServer中处理客户端消息的函数对象的超类。在GameServer中处理客户端消息的函数类
 * 需要继承此方法。
 *
 * @author pangchong
 *
 * @param <Msg>
 * @param <Args1>
 * @param <Args2>
 */
public abstract class AbstractClientMsgFunc<Msg extends GeneratedMessage, Args1 extends MsgArgs, Args2 extends MsgArgs>
        implements IClientMsgFunc<Msg, Args1, Args2> {

    @Override
    public void handle(ISession session, Msg msg, Args1 arg1, Args2 arg2) {
        this.handle((Player) session, msg, arg1, arg2);
    }

    public abstract void handle(Player player, Msg msg, Args1 arg1, Args2 arg2);
}
