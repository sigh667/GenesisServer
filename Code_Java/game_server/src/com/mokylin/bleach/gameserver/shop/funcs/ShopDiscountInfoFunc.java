package com.mokylin.bleach.gameserver.shop.funcs;

import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.core.isc.remote.IRemote;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.server.IServerMsgFunc;
import com.mokylin.bleach.gamedb.uuid.UUIDType;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.shop.discount.ShopDiscount;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;
import com.mokylin.bleach.servermsg.gameserver.shop.ShopDiscountInfo;

/**
 * 添加物品打折信息的处理器
 * @author Administrator
 *
 */

public class ShopDiscountInfoFunc implements IServerMsgFunc<ShopDiscountInfo, ServerGlobals, MsgArgs> {

	@Override
	public void handle(IRemote remote, ShopDiscountInfo msg,
			ServerGlobals sGlobals, MsgArgs arg2) {
		if(msg.serverId != sGlobals.getServerId()) return;
		
		ShopType shopType = ShopType.getByIndex(msg.shopTypeId);
		if(shopType == null) return;
		
		if(msg.discount <= 0) return;
		
		if(msg.numMultiple < 1) return;
		
		if(msg.startTime == null || msg.endTime == null) return;
		
		if(msg.startTime.getTime() > msg.endTime.getTime()) return;
		
		long nextShopDiscountUUID = sGlobals.getUUIDGenerator().getNextUUID(UUIDType.ShopDiscount);
		
		ShopDiscount shopDiscount = new ShopDiscount(sGlobals, nextShopDiscountUUID, msg.serverId, shopType, msg.discount, msg.numMultiple, msg.startTime.getTime(), msg.endTime.getTime());
		
		sGlobals.getDiscountService().addDiscountInfo(shopDiscount, sGlobals);
	}

	@Override
	public MessageTarget getTarget() {
		return MessageTarget.SERVER;
	}

}
