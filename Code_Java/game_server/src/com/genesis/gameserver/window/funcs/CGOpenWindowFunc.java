package com.genesis.gameserver.window.funcs;

import com.genesis.common.window.Window;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.player.Player;
import com.genesis.protobuf.WindowMessage.CGOpenWindow;

/**
 * 处理打开窗口的消息对象
 *
 * 该函数对象在PlayerActor中执行
 *
 * @author yaguang.xiao
 *
 */
public class CGOpenWindowFunc extends AbstractClientMsgFunc<CGOpenWindow, Human, ServerGlobals> {

    @Override
    public void handle(Player player, CGOpenWindow msg, Human human, ServerGlobals sGlobals) {
        Window window = Window.getByIndex(msg.getWindowTypeId());
        if (window == null) {
            human.notifyDataErrorAndDisconnect();
        }

        human.getWindowManager().open(window);
    }

}
