package com.genesis.gameserver.core.msgfunc.playeractor;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.player.Player;
import com.google.protobuf.GeneratedMessage;

/**
 * 专门为PlayerActor处理的消息处理定义一个基类
 * @author Joey
 *
 * @param <Msg>
 */
public abstract class PlayerActorMsgFunc<Msg extends GeneratedMessage>
        extends AbstractClientMsgFunc<Msg, Human, ServerGlobals> {

    @Override
    public void handle(Player player, Msg msg, Human human, ServerGlobals sGlobals) {
        this.process(player, msg, human, sGlobals);
    }

    public abstract void process(Player player, Msg msg, Human human, ServerGlobals sGlobals);
}
