package com.genesis.gameserver.shop.funcs;

import com.genesis.common.core.GlobalData;
import com.genesis.common.currency.Currency;
import com.genesis.common.human.HumanPropId;
import com.genesis.common.shop.ShopType;
import com.genesis.common.shop.template.ShopRefreshPriceTemplate;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.genesis.gameserver.player.Player;
import com.genesis.gameserver.shop.shop.Shop;
import com.genesis.protobuf.ShopMessage.CGRefreshGoods;
import com.genesis.protobuf.ShopMessage.ShopPrompt;

/**
 * 处理商店手动刷新消息的函数对象
 *
 * 该函数对象在PlayerActor中执行
 *
 * @author yaguang.xiao
 *
 */
public class CGRefreshGoodsFunc
        extends AbstractClientMsgFunc<CGRefreshGoods, Human, ServerGlobals> {

    @Override
    public void handle(Player player, CGRefreshGoods msg, Human human, ServerGlobals sGlobals) {
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

        ShopRefreshPriceTemplate nextRefreshPrice = GlobalData.getTemplateService().
                get(shop.getShopRefresh().getMauallyRefresh().getManuallyRefreshCount() + 1,
                        ShopRefreshPriceTemplate.class);

        if (nextRefreshPrice == null) {
            human.sendMessage(
                    MessageBuilder.buildShopPrompt(ShopPrompt.ALREADY_REACH_REFRESH_UPPER_LIMIT));
            return;
        }

        Currency mainShopCurrency = shop.getShopTemplate().getMainShopCurrency();
        long price = nextRefreshPrice.getPrice(shop.getShopType());

        if (!human.isMoneyEnough(mainShopCurrency, price)) {
            human.sendMessage(MessageBuilder.buildShopPrompt(ShopPrompt.MONEY_NOT_ENOUGH));
            return;
        }

        // 扣钱
        human.costMoney(mainShopCurrency, price);

        // 手动刷新
        shop.getShopRefresh().getMauallyRefresh()
                .mauallyRefresh((int) human.get(HumanPropId.LEVEL));

        // 发送商店刷新消息
        human.sendMessage(
                MessageBuilder.buildShopManuallyRefreshInfo(shop, mainShopCurrency, price));
    }

}
