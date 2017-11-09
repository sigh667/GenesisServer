package com.mokylin.bleach.gameserver.shop.timeevent;

import com.mokylin.bleach.core.timeaxis.ITimeEvent;
import com.mokylin.bleach.core.timeaxis.ITimeEventType;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.shop.shop.Shop;
import com.mokylin.bleach.protobuf.ShopMessage.GCShopClose;

/**
 * 商店关闭任务
 * @author yaguang.xiao
 *
 */
public class ShopCloseTask implements ITimeEvent<Human> {

	private final Shop shop;
	
	public ShopCloseTask(Shop shop) {
		this.shop = shop;
	}
	
	@Override
	public ITimeEventType getEventType() {
		return ShopTimeEventType.SHOP_CLOSE;
	}

	@Override
	public long getEventId() {
		return this.shop.getShopType().getIndex();
	}

	@Override
	public void eventOccur(Human human) {
		shop.close();
		
		// 发送商店关闭消息
		GCShopClose.Builder msgB = GCShopClose.newBuilder();
		msgB.setShopTypeId(this.shop.getShopType().getIndex());
		human.sendMessage(msgB);
	}

}
