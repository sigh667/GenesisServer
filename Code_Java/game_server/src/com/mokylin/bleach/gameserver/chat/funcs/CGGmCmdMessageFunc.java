package com.mokylin.bleach.gameserver.chat.funcs;

import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.ChatMessage.CGGmCmdMessage;

/**
 * 执行客户端发来的GM命令的函数对象。<p>
 * 
 * 该函数对象在PlayerActor中执行。
 * 
 * @author pangchong
 *
 */
public class CGGmCmdMessageFunc extends AbstractClientMsgFunc<CGGmCmdMessage, Human, ServerGlobals> {

	@Override
	public void handle(Player player, CGGmCmdMessage msg, Human human, ServerGlobals sGlobals) {
		if(!player.isGmAccount()) return;
		Globals.getGmCmdFuncService().handleGmCmd(msg.getCmd(), msg.getParamList(), human, sGlobals);
	}

}
