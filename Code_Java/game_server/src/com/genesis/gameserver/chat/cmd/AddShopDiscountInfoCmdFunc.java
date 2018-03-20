package com.genesis.gameserver.chat.cmd;

import com.genesis.common.shop.ShopType;
import com.genesis.gameserver.chat.cmd.core.IGmCmdFunction;
import com.genesis.gameserver.core.global.ServerGlobals;
import com.genesis.gameserver.human.Human;
import com.mokylin.bleach.gamedb.uuid.UUIDType;
import com.genesis.gameserver.shop.discount.ShopDiscount;

import java.sql.Timestamp;
import java.util.List;

/**
 * 添加商店折扣信息的GM指令
 *
 * 格式：shopdiscount serverId[正整数] shopTypeId[整数] discount[正整数] numMultiple[正整数] startTime[格式：2014-12-25_09:25:33] endTime[格式：2014-12-25_09:25:33]
 *
 * @author yaguang.xiao
 *
 */
public class AddShopDiscountInfoCmdFunc implements IGmCmdFunction {

    @Override
    public String getGmCmd() {
        return "shopdiscount";
    }

    @Override
    public void handle(List<String> paramList, Human human, ServerGlobals sGlobals) {
        if (paramList == null || paramList.size() != 6) {
            return;
        }

        int serverId = Integer.parseInt(paramList.get(0));
        if (serverId != sGlobals.getServerId()) {
            return;
        }

        ShopType shopType = ShopType.getByIndex(Integer.parseInt(paramList.get(1)));
        if (shopType == null) {
            return;
        }

        int discount = Integer.parseInt(paramList.get(2));
        if (discount <= 0) {
            return;
        }

        int numMultiple = Integer.parseInt(paramList.get(3));
        if (numMultiple < 1) {
            return;
        }

        Timestamp startTime = Timestamp.valueOf(paramList.get(4).replace('_', ' '));
        Timestamp endTime = Timestamp.valueOf(paramList.get(5).replace('_', ' '));
        if (startTime.getTime() > endTime.getTime()) {
            return;
        }

        long nextShopDiscountUUID = sGlobals.getUUIDGenerator().getNextUUID(UUIDType.ShopDiscount);

        ShopDiscount shopDiscount =
                new ShopDiscount(sGlobals, nextShopDiscountUUID, serverId, shopType, discount,
                        numMultiple, startTime.getTime(), endTime.getTime());

        sGlobals.getDiscountService().addDiscountInfo(shopDiscount, sGlobals);
    }

}
