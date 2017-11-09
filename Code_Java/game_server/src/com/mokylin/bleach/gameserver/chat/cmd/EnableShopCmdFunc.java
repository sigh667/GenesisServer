package com.mokylin.bleach.gameserver.chat.cmd;

import java.util.List;

import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.gameserver.chat.cmd.core.IGmCmdFunction;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.shop.shop.Shop;

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
	public void handle(List<String> paramList, Human human,
			ServerGlobals sGlobals) {
		if (paramList == null || paramList.size() != 1)
			return;

		int shopTypeId = Integer.parseInt(paramList.get(0));

		if (!ShopType.isValid(shopTypeId))
			return;
		
		ShopType shopType = ShopType.getByIndex(shopTypeId);
		human.getFuncManager().openFunc(shopType.getFuncType());
		
		Shop shop = human.getShopManager().enableShop(shopType);
		if(shop == null) {
			return;
		}
		
		if(!shop.isOpenForever()) {
			human.getShopManager().sendOpenShopMessage(shopType);
		}
	}

}
