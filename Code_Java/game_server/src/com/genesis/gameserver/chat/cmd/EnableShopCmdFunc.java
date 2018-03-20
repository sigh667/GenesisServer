package com.genesis.gameserver.chat.cmd;

import com.genesis.common.shop.ShopType;
import com.genesis.gameserver.chat.cmd.core.IGmCmdFunction;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.shop.shop.Shop;

import java.util.List;

/**
 * 开启商店的GM指令
 *
 * 格式：enableshop shopTypeId[整数]
 *
 * @author yaguang.xiao
 *
 */
public class EnableShopCmdFunc implements IGmCmdFunction {

    @Override
    public String getGmCmd() {
        return "enableshop";
    }

    @Override
    public void handle(List<String> paramList, Human human, ServerGlobals sGlobals) {
        if (paramList == null || paramList.size() != 1) {
            return;
        }

        int shopTypeId = Integer.parseInt(paramList.get(0));

        if (!ShopType.isValid(shopTypeId)) {
            return;
        }

        ShopType shopType = ShopType.getByIndex(shopTypeId);
        human.getFuncManager().openFunc(shopType.getFuncType());

        Shop shop = human.getShopManager().enableShop(shopType);
        if (shop == null) {
            return;
        }

        if (!shop.isOpenForever()) {
            human.getShopManager().sendOpenShopMessage(shopType);
        }
    }

}
