package com.mokylin.bleach.gameserver.chat.cmd;

import java.util.List;

import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.gameserver.chat.cmd.core.IGmCmdFunction;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.shop.shop.Shop;

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
	public void handle(List<String> paramList, Human human,
			ServerGlobals sGlobals) {
		if(paramList == null || paramList.isEmpty()) return;
		
		int shopTypeId = Integer.parseInt(paramList.get(0));
		
		if(!ShopType.isValid(shopTypeId)) return;
		
		Shop shop = human.getShopManager().getShop(ShopType.getByIndex(shopTypeId));
		
		if(shop == null) return;
		
		shop.getShopRefresh().getMauallyRefresh().simplyExecute(Globals.getTimeService().now(), human);
	}

}
