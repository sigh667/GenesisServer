package com.mokylin.bleach.gameserver.item.funcs;

import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.protobuf.ItemMessage.CGItemUse;

/**
 * 处理客户端使用物品消息的函数对象。
 * 
 * 该函数对象在PlayerActor中执行。
 * 
 * @author pangchong
 *
 */
public class CGItemUseFunc extends AbstractClientMsgFunc<CGItemUse, Human, ServerGlobals>{

	@Override
	public void handle(Player player, CGItemUse msg, Human human, ServerGlobals sGlobals) {
		human.getInventory().itemUse(msg.getTemplateId(), msg.getAmount());		
	}

}
