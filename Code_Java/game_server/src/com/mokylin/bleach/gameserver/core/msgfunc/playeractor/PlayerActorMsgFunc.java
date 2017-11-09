package com.mokylin.bleach.gameserver.core.msgfunc.playeractor;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.player.Player;

/**
 * 专门为PlayerActor处理的消息处理定义一个基类
 * @author baoliang.shen
 *
 * @param <Msg>
 */
public abstract class PlayerActorMsgFunc<Msg extends GeneratedMessage> extends AbstractClientMsgFunc<Msg, Human, ServerGlobals> {

	@Override
	public void handle(Player player, Msg msg, Human human, ServerGlobals sGlobals) {
		this.process(player, msg, human, sGlobals);
	}

	public abstract void process(Player player, Msg msg, Human human, ServerGlobals sGlobals);
}
