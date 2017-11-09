package com.mokylin.bleach.gameserver.shop.funcs;

import java.util.List;

import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.currency.Price;
import com.mokylin.bleach.gameserver.shop.shop.Shop;
import com.mokylin.bleach.protobuf.CommonMessage.CurrencyChangeInfo;
import com.mokylin.bleach.protobuf.ShopMessage.GCGoodBuySuccess;
import com.mokylin.bleach.protobuf.ShopMessage.GCShopAutoRefreshInfo;
import com.mokylin.bleach.protobuf.ShopMessage.GCShopInfo;
import com.mokylin.bleach.protobuf.ShopMessage.GCShopManuallyRefreshInfo;
import com.mokylin.bleach.protobuf.ShopMessage.GCShopPrompt;
import com.mokylin.bleach.protobuf.ShopMessage.GoodInfo;
import com.mokylin.bleach.protobuf.ShopMessage.ShopPrompt;

/**
 * 消息构建器
 * @author yaguang.xiao
 *
 */
public class MessageBuilder {

	/**
	 * 构建商店信息消息
	 * @param shop
	 * @return
	 */
	public static GCShopInfo buildShopInfo(Shop shop) {
		if(shop == null) {
			return null;
		}
		
		GCShopInfo.Builder gcShopInfoB = GCShopInfo.newBuilder();
		gcShopInfoB.setShopTypeId(shop.getShopType().getIndex());
		gcShopInfoB.addAllCurrencyList(shop.getShopType().getCurrencyList());
		gcShopInfoB.setIsOpenForever(shop.isOpenForever());
		gcShopInfoB.setSecondsBeforeNextAutoRefreshTime(shop.getShopRefresh().getAutoRefresh().getSecondsBeforeNextAutoExecuteTime());
		gcShopInfoB.setLeftOpenTime(shop.getLeftOpenTime());
		gcShopInfoB.addAllGoodInfoList(shop.buildGoodInfoList());
		gcShopInfoB.setGoodBornTime(shop.getGoodBornTime());
		gcShopInfoB.setManuallyRefreshCount(shop.getShopRefresh().getMauallyRefresh().getManuallyRefreshCount());
		
		return gcShopInfoB.build();
	}
	
	/**
	 * 构造商店手动刷新消息
	 * @param shop
	 * @param mainShopCurrency
	 * @param price
	 */
	public static GCShopManuallyRefreshInfo buildShopManuallyRefreshInfo(Shop shop, Currency mainShopCurrency, long price) {
		CurrencyChangeInfo.Builder currencyChangeInfoB = CurrencyChangeInfo.newBuilder();
		currencyChangeInfoB.setCurrencyTypeId(mainShopCurrency.getIndex());
		currencyChangeInfoB.setChangeValue(- price);
		
		List<GoodInfo> goodInfoList = shop.buildGoodInfoList();
		
		GCShopManuallyRefreshInfo.Builder shopManuallyRefreshInfoB = GCShopManuallyRefreshInfo.newBuilder();
		shopManuallyRefreshInfoB.setCurrencyChange(currencyChangeInfoB);
		shopManuallyRefreshInfoB.addAllGoodInfoList(goodInfoList);
		shopManuallyRefreshInfoB.setGoodBornTime(shop.getGoodBornTime());
		
		return shopManuallyRefreshInfoB.build();
	}
	
	/**
	 * 构造商店自动刷新消息
	 * @param shop
	 * @return
	 */
	public static GCShopAutoRefreshInfo buildShopAutoRefreshInfo(Shop shop) {
		GCShopAutoRefreshInfo.Builder msgB = GCShopAutoRefreshInfo.newBuilder();
		
		msgB.addAllGoodInfoList(shop.buildGoodInfoList());
		msgB.setGoodBornTime(shop.getGoodBornTime());
		msgB.setSecondsBeforeNextAutoRefreshTime(shop.getShopRefresh().getAutoRefresh().getSecondsBeforeNextAutoExecuteTime());
		
		return msgB.build();
	}
	
	/**
	 * 构建货物都买成功消息
	 * @param itemPrice
	 * @param position
	 * @return
	 */
	public static GCGoodBuySuccess buildBuyGoodSucMsg(Price itemPrice, int position) {
		GCGoodBuySuccess.Builder goodBuySucMsgB = GCGoodBuySuccess.newBuilder();
		
		CurrencyChangeInfo.Builder currencyChangeInfoB = CurrencyChangeInfo.newBuilder();
		currencyChangeInfoB.setCurrencyTypeId(itemPrice.currency.getIndex());
		currencyChangeInfoB.setChangeValue(- itemPrice.price);
		goodBuySucMsgB.setCurrencyChange(currencyChangeInfoB);
		
		goodBuySucMsgB.setPosition(position);
		
		return goodBuySucMsgB.build();
	}
	
	/**
	 * 构造商店提示
	 * @param langId
	 * @return
	 */
	public static GCShopPrompt buildShopPrompt(ShopPrompt prompt) {
		GCShopPrompt.Builder msgB = GCShopPrompt.newBuilder();
		msgB.setPrompt(prompt);
		return msgB.build();
	}
	
}
