package com.mokylin.bleach.gameserver.shop.timeevent;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.LocalTime;

import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.core.timeaxis.ITimeEventType;
import com.mokylin.bleach.core.timeaxis.TimeAxis;
import com.mokylin.bleach.gameserver.core.autoexecutetask.AbstractAutoExecuteTaskWithHandleObjectInSql;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.shop.funcs.MessageBuilder;
import com.mokylin.bleach.gameserver.shop.shop.Shop;

/**
 * 商店自动刷新货物
 * @author yaguang.xiao
 *
 */
public class ShopAutoRefresh extends AbstractAutoExecuteTaskWithHandleObjectInSql<Human> {

	private final Shop shop;
	
	public ShopAutoRefresh(List<LocalTime> autoExecuteTimeList,
			Timestamp lastExecuteTime, Shop shop,
			TimeAxis<Human> timeAxis) {
		super(autoExecuteTimeList, timeAxis, lastExecuteTime.getTime(), shop);
		this.shop = shop;
	}

	@Override
	public ITimeEventType getEventType() {
		return ShopTimeEventType.SHOP_AUTO_REFRESH;
	}

	@Override
	public long getEventId() {
		return this.shop.getShopType().getIndex();
	}

	@Override
	protected boolean isCanAutoExecute(Human human) {
		return human.getWindowManager().isOpen(this.shop.getShopType().getWindow());
	}

	@Override
	protected void simplyExecute(long executeTime, Human human) {
		this.shop.getShopRefresh().refresh((int) human.get(HumanPropId.LEVEL), executeTime);
	}

	@Override
	protected void sendMessage(Human human) {
		human.sendMessage(MessageBuilder.buildShopAutoRefreshInfo(shop));
	}

}
