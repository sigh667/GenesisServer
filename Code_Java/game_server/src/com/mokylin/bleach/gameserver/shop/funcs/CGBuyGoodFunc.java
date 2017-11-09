package com.mokylin.bleach.gameserver.shop.funcs;

import com.mokylin.bleach.common.currency.Price;
import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.core.msgfunc.AbstractClientMsgFunc;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.player.Player;
import com.mokylin.bleach.gameserver.shop.shop.Good;
import com.mokylin.bleach.gameserver.shop.shop.Shop;
import com.mokylin.bleach.protobuf.ShopMessage.CGBuyGood;
import com.mokylin.bleach.protobuf.ShopMessage.ShopPrompt;

/**
 * 处理购买商品消息的函数对象
 * 
 * 该函数对象在PlayerActor中执行
 * 
 * @author yaguang.xiao
 *
 */
public class CGBuyGoodFunc extends AbstractClientMsgFunc<CGBuyGood, Human, ServerGlobals> {

	@Override
	public void handle(Player player, CGBuyGood msg, Human human,
			ServerGlobals sGlobals) {
		ShopType shopType = ShopType.getByIndex(msg.getShopTypeId());
		if(shopType == null) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		if(!human.getFuncManager().isOpenFunction(shopType.getFuncType())) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		Shop shop = human.getShopManager().getShop(shopType);
		if(shop == null) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		if(!shop.isOpen()) {
			human.sendMessage(MessageBuilder.buildShopPrompt(ShopPrompt.SHOP_CLOSED));
			return;
		}
		
		if(shop.isRefreshed(msg.getCurGoodBornTime())) {
			human.sendMessage(MessageBuilder.buildShopPrompt(ShopPrompt.GOODS_ALREADY_REFRESHED));
			return;
		}
		
		Good good = shop.getGood(msg.getGoodPosition());
		
		if(good == null) {
			human.notifyDataErrorAndDisconnect();
			return;
		}
		
		if(!good.isSelling()) {
			human.sendMessage(MessageBuilder.buildShopPrompt(ShopPrompt.GOODS_ALREADY_SELL_UP));
			return;
		}
		
		Price price = good.getPrice();
		
		if(!human.isMoneyEnough(price.currency, price.price)) {
			human.sendMessage(MessageBuilder.buildShopPrompt(ShopPrompt.MONEY_NOT_ENOUGH));
			return;
		}
		
		human.costMoney(price.currency, price.price);
		
		// 把商店中的物品设置为已买
		good.soldOut();
		
		// 执行物品入包操作
		if (!human.getInventory().addItem(good.getItemTemplate().getId(), good.getNum())) {
			// TODO 记录日志，货物入包失败
		}
		
		// 发送货物购买成功的消息
		human.sendMessage(MessageBuilder.buildBuyGoodSucMsg(price, msg.getGoodPosition()));
	}

}
