package com.genesis.gameserver.window.funcs;

import com.genesis.common.window.Window;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.player.Player;
import com.genesis.protobuf.WindowMessage.CGCloseWindow;

/**
 * 处理关闭窗口的消息对象
 *
 * 该函数对象在PlayerActor中执行
 *
 * @author yaguang.xiao
 *
 */
public class CGCloseWindowFunc extends AbstractClientMsgFunc<CGCloseWindow, Human, ServerGlobals> {

    @Override
    public void handle(Player player, CGCloseWindow msg, Human human, ServerGlobals sGlobals) {
        Window window = Window.getByIndex(msg.getWindowTypeId());
        if (window == null) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        human.getWindowManager().close(window);
    }

}
