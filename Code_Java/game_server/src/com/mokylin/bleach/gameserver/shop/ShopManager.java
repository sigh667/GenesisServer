package com.mokylin.bleach.gameserver.shop;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.common.shop.template.ShopTemplate;
import com.mokylin.bleach.gamedb.orm.entity.ShopEntity;
import com.mokylin.bleach.gamedb.uuid.UUIDType;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.shop.shop.Shop;
import com.mokylin.bleach.gameserver.shop.timeevent.ShopCloseTask;
import com.mokylin.bleach.protobuf.ShopMessage.GCOpenShop;
import com.mokylin.bleach.protobuf.ShopMessage.GCOpenedTempShop;

/**
 * 商店管理器
 * @author yaguang.xiao
 *
 */
public class ShopManager {
	
	private Human human;
	/** 所有开过的商店 */
	private Map<ShopType, Shop> shops = Maps.newHashMap();
	
	public ShopManager(Human human) {
		this.human = human;
	}
	
	/**
	 * 加载商店信息
	 * @param shopEntityList
	 */
	public void loadFromEntity(List<ShopEntity> shopEntityList) {
		if(shopEntityList == null || shopEntityList.isEmpty()) {
			return;
		}
		
		for(ShopEntity entity : shopEntityList) {
			if(entity == null) {
				continue;
			}
			
			Shop shop = new Shop(human);
			shop.fromEntity(entity);
			
			shops.put(shop.getShopType(), shop);
		}
	}
	
	/**
	 * 创建好玩家角色的时候执行的方法
	 */
	public void initWhenNewCreateHuman() {
		for (ShopType shopType : ShopType.values()) {
			if(this.human.getFuncManager().isOpenFunction(shopType.getFuncType())) {
				this.enableShop(shopType);
			}
		}
	}
	
	/**
	 * 初始化
	 */
	public void init() {
		this.scheduleTimeEvent();
	}
	
	/**
	 * 调度时间事件
	 */
	private void scheduleTimeEvent() {
		for(Shop shop : this.shops.values()) {
			// 重置手动刷新次数
			shop.getShopRefresh().getMauallyRefresh().start(human);
			
			if(!shop.isOpen()) {
				continue;
			}
			
			if(shop.isOpenForever()) {
				// 自动刷新
				shop.getShopRefresh().getAutoRefresh().start(human);
			} else {
				human.getTimeAxis().scheduleEventOnThisTime(new ShopCloseTask(shop), shop.getCloseTime());
			}
		}
	}
	
	/**
	 * 登陆的时候通知
	 */
	public void notifyOnLogin() {
		GCOpenedTempShop.Builder openedShopBuilder = GCOpenedTempShop.newBuilder();
		for(Shop shop : this.shops.values()) {
			if(!shop.isOpen() || shop.isOpenForever()) {
				continue;
			}
			
			openedShopBuilder.addShopTypeList(shop.getShopType().getIndex());
		}
		this.human.sendMessage(openedShopBuilder);
	}
	
	/**
	 * 获取指定类型的商店
	 * @param shopType
	 * @return
	 */
	public Shop getShop(ShopType shopType) {
		return this.shops.get(shopType);
	}
	
	/**
	 * 激活商店
	 * @param shopType
	 * @return
	 */
	public Shop enableShop(ShopType shopType) {
		if(shopType == null) return null;
		
		Shop shop = this.shops.get(shopType);
		if(shop == null) {
			ShopTemplate shopTmpl = GlobalData.getTemplateService().get(shopType.getIndex(), ShopTemplate.class);
			long tempOpenDuration = shopTmpl.getTempOpenDuration() * 60 * 1000;// 把分钟转成毫秒
			Timestamp closeTime = new Timestamp(Globals.getTimeService().now() + tempOpenDuration);
			long shopUUID = this.human.getServerGlobals().getUUIDGenerator().getNextUUID(UUIDType.Shop);
			shop = new Shop(this.human, shopUUID, shopType, closeTime);
		} else if (shop.isOpen()) {
			return null;
		}
		
		if(shop.isOpenForever()) {
			shop.getShopRefresh().getAutoRefresh().triggerExecute(human);
			shop.getShopRefresh().getAutoRefresh().start(human);
		} else {
			long now = Globals.getTimeService().now();
			shop.getShopRefresh().refresh((int)human.get(HumanPropId.LEVEL), now);
			shop.setDeadline(shop.getShopTemplate().getTempOpenDuration());
			shop.open();
			shop.setModified();
			human.getTimeAxis().scheduleEventOnThisTime(new ShopCloseTask(shop), shop.getCloseTime());
		}
		shop.getShopRefresh().getMauallyRefresh().start(human);
		this.shops.put(shopType, shop);
		
		return shop;
	}
	
	/**
	 * 发送出现商店的消息
	 * @param shopType
	 */
	public void sendOpenShopMessage(ShopType shopType) {
		GCOpenShop.Builder openShopMsg = GCOpenShop.newBuilder();
		openShopMsg.setShopTypeId(shopType.getIndex());
		human.sendMessage(openShopMsg);
	}
	
}
