package com.genesis.gameserver.chat.funcs;

import com.genesis.gameserver.core.global.Globals;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.player.Player;
import com.genesis.protobuf.ChatMessage.CGGmCmdMessage;

/**
 * 执行客户端发来的GM命令的函数对象。<p>
 *
 * 该函数对象在PlayerActor中执行。
 *
 * @author pangchong
 *
 */
public class CGGmCmdMessageFunc
        extends AbstractClientMsgFunc<CGGmCmdMessage, Human, ServerGlobals> {

    @Override
    public void handle(Player player, CGGmCmdMessage msg, Human human, ServerGlobals sGlobals) {
        if (!player.isGmAccount()) {
            return;
        }
        Globals.getGmCmdFuncService()
                .handleGmCmd(msg.getCmd(), msg.getParamList(), human, sGlobals);
    }

}
