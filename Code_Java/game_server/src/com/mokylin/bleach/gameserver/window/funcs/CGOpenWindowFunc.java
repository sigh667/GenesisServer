package com.mokylin.bleach.gameserver.window.funcs;

import com.mokylin.bleach.common.window.Window;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.WindowMessage.CGOpenWindow;

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
	public void handle(Player player, CGOpenWindow msg, Human human,
			ServerGlobals sGlobals) {
		Window window = Window.getByIndex(msg.getWindowTypeId());
		if(window == null) {
			human.notifyDataErrorAndDisconnect();
		}
		
		human.getWindowManager().open(window);
	}

}
