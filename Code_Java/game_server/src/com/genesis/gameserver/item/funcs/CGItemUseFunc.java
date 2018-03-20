package com.genesis.gameserver.item.funcs;

import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.player.Player;
import com.genesis.protobuf.ItemMessage.CGItemUse;

/**
 * 处理客户端使用物品消息的函数对象。
 *
 * 该函数对象在PlayerActor中执行。
 *
 * @author pangchong
 *
 */
public class CGItemUseFunc extends AbstractClientMsgFunc<CGItemUse, Human, ServerGlobals> {

    @Override
    public void handle(Player player, CGItemUse msg, Human human, ServerGlobals sGlobals) {
        human.getInventory().itemUse(msg.getTemplateId(), msg.getAmount());
    }

}
