package com.genesis.gameserver.chat.cmd;

import com.genesis.common.shop.ShopType;
import com.genesis.gameserver.chat.cmd.core.IGmCmdFunction;
import com.genesis.gameserver.core.global.Globals;
import com.genesis.gameserver.human.Human;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.shop.shop.Shop;

import java.util.List;

/**
 * 重置商店手动刷新数量的GM命令。<p>
 *
 * 格式：resetshopmanuallyrefreshcount shopTypeId[整数]
 *
 * @author pangchong
 *
 */
public class ResetShopManuallyRefreshCountCmd implements IGmCmdFunction {

    @Override
    public String getGmCmd() {
        return "resetshopmanuallyrefreshcount";
    }

    @Override
    public void handle(List<String> paramList, Human human, ServerGlobals sGlobals) {
        if (paramList == null || paramList.isEmpty()) {
            return;
        }

        int shopTypeId = Integer.parseInt(paramList.get(0));

        if (!ShopType.isValid(shopTypeId)) {
            return;
        }

        Shop shop = human.getShopManager().getShop(ShopType.getByIndex(shopTypeId));

        if (shop == null) {
            return;
        }

        shop.getShopRefresh().getMauallyRefresh()
                .simplyExecute(Globals.getTimeService().now(), human);
    }

}
