package com.genesis.gameserver.shop.funcs;

import com.genesis.common.shop.ShopType;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.player.Player;
import com.genesis.gameserver.shop.shop.Shop;
import com.genesis.protobuf.ShopMessage.CGOpenShop;
import com.genesis.protobuf.ShopMessage.ShopPrompt;

/**
 * 处理客户端打开商店消息的函数对象
 *
 * 该函数对象在PlayerActor中执行
 *
 * @author yaguang.xiao
 *
 */
public class CGOpenShopFunc extends AbstractClientMsgFunc<CGOpenShop, Human, ServerGlobals> {

    @Override
    public void handle(Player player, CGOpenShop msg, Human human, ServerGlobals sGlobals) {
        ShopType shopType = ShopType.getByIndex(msg.getShopTypeId());
        if (shopType == null) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        if (!human.getFuncManager().isOpenFunction(shopType.getFuncType())) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        Shop shop = human.getShopManager().getShop(shopType);
        if (shop == null) {
            human.notifyDataErrorAndDisconnect();
            return;
        }

        if (!shop.isOpen()) {
            human.sendMessage(MessageBuilder.buildShopPrompt(ShopPrompt.SHOP_CLOSED));
            return;
        }

        shop.onOpenShop(human);
        // 打开商店窗口
        human.getWindowManager().open(shop.getShopType().getWindow());

        // 向客户端发送商店信息
        human.sendMessage(MessageBuilder.buildShopInfo(shop));
    }

}
