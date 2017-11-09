package com.mokylin.bleach.gameserver.shop.shop;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.LocalTime;

import com.google.common.collect.Lists;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.common.shop.template.ShopGoodTemplate;
import com.mokylin.bleach.common.shop.template.ShopItemStoreRoomTemplate;
import com.mokylin.bleach.core.util.RandomUtil;
import com.mokylin.bleach.core.util.TimeUtils;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.shop.discount.ShopDiscount;
import com.mokylin.bleach.gameserver.shop.discount.ShopDiscountService;
import com.mokylin.bleach.gameserver.shop.timeevent.ShopAutoRefresh;
import com.mokylin.bleach.gameserver.shop.timeevent.ShopManuallyRefresh;

/**
 * 商店刷新
 * @author yaguang.xiao
 *
 */
public class ShopRefresh {
	
	/** 商店 */
	private Shop shop;
	
	/** 商店手动刷新 */
	private ShopManuallyRefresh mauallyRefresh;
	/** 商店自动刷新 */
	private ShopAutoRefresh autoRefresh;
	
	/** 物品打折服务 */
	private final ShopDiscountService discountService;
	
	/**
	 * 第一次创建商店的时候调用
	 * @param shop
	 * @param human
	 */
	public ShopRefresh(Shop shop, Human human) {
		this.shop = shop;
		this.discountService = human.getServerGlobals().getDiscountService();
		
		Timestamp lastResetManuallyRefreshCountTime = new Timestamp(TimeUtils.TIMESTAMP_INITIAL_VALUE);
		this.mauallyRefresh = new ShopManuallyRefresh(shop.getShopType().getAutoResetManuallyRefreshTime(),
				human.getTimeAxis(), shop, 0, lastResetManuallyRefreshCountTime);

		List<LocalTime> autoRefreshTimes = Globals.getShopRelatedTemplateBufferData().getAutoRefreshTime(shop.getShopType());
		Timestamp lastAutoRefreshTime = new Timestamp(TimeUtils.TIMESTAMP_INITIAL_VALUE);
		this.autoRefresh = new ShopAutoRefresh(autoRefreshTimes, lastAutoRefreshTime, shop, human.getTimeAxis());
	}
	
	/**
	 * 从数据库加载的时候调用
	 * @param shop
	 * @param human
	 * @param manuallyRefreshCount
	 * @param lastResetManuallyRefreshCountTime
	 * @param lastAutoRefreshTime
	 */
	public ShopRefresh(Shop shop, Human human, int manuallyRefreshCount,
			Timestamp lastResetManuallyRefreshCountTime, Timestamp lastAutoRefreshTime) {
		this.shop = shop;
		this.discountService = human.getServerGlobals().getDiscountService();
		
		this.mauallyRefresh = new ShopManuallyRefresh(shop.getShopType().getAutoResetManuallyRefreshTime(),
				human.getTimeAxis(), shop, manuallyRefreshCount, lastResetManuallyRefreshCountTime);
		
		List<LocalTime> autoRefreshTimes = Globals.getShopRelatedTemplateBufferData().getAutoRefreshTime(shop.getShopType());
		this.autoRefresh = new ShopAutoRefresh(autoRefreshTimes, lastAutoRefreshTime, shop, human.getTimeAxis());
	}
	
	public ShopAutoRefresh getAutoRefresh() {
		return this.autoRefresh;
	}
	
	public ShopManuallyRefresh getMauallyRefresh() {
		return this.mauallyRefresh;
	}
	
	/**
	 * 刷新商店
	 * @param humanLevel
	 */
	public void refresh(int humanLevel, long now) {
		List<ShopGoodTemplate> shopGoodTmpls = Globals.getShopRelatedTemplateBufferData().getShopGoodTmplList(this.shop.getShopType());
		List<Good> newGoodList = Lists.newArrayListWithCapacity(shopGoodTmpls.size());
		Good[] newGoods = new Good[shopGoodTmpls.size()];
		for(ShopGoodTemplate tmpl : shopGoodTmpls) {
			newGoods[tmpl.getSellPosition()] = randomSelectGood(tmpl, humanLevel);
		}
		
		for(Good good : newGoods) {
			newGoodList.add(good);
		}
		
		this.shop.setGoodList(newGoodList, now);
	}
	
	/**
	 * 随机选择商品
	 * @param shopGoodTmpl	商店货物模板
	 * @param humanLevel	玩家角色等级
	 * @return
	 */
	private Good randomSelectGood(ShopGoodTemplate shopGoodTmpl, int humanLevel) {
		ShopItemStoreRoomTemplate storeRoomTmpl = GlobalData.getTemplateService().
				get(shopGoodTmpl.getStoreRoomId(), ShopItemStoreRoomTemplate.class);
		int critRate = RandomUtil.randomSuccess(shopGoodTmpl.getCritChance()) ? shopGoodTmpl.getCritRate() : 1;
		ShopDiscount shopDiscount = this.discountService.getShopDiscount(shopGoodTmpl.getShopType());
		boolean discounting = shopDiscount != null && shopDiscount.isDiscounting();
		int discount = discounting ? shopDiscount.getDiscount() : ShopDiscountService.NO_DISCOUNT;
		int shopDiscountNumMultiple = discounting ? shopDiscount.getNumMultiple() : ShopDiscountService.NO_MULTIPLE;
		int finalGoodNum = shopGoodTmpl.getBaseNum() * critRate * storeRoomTmpl.getNumberMultiple() * shopDiscountNumMultiple;
		if(finalGoodNum <= 0) {
			throw new RuntimeException(String.format("随机刷出的商品数量必须大于0！【ShopGoodTemplateId:%d】【humanLevel:%d】", shopGoodTmpl.getId(), humanLevel));
		}
		
		int levelScope = storeRoomTmpl.getItemLevelScope();
		int minLevel = humanLevel - levelScope;
		if(minLevel < Human.MIN_LEVEL) {
			minLevel = Human.MIN_LEVEL;
		}
		int maxLevel = humanLevel + levelScope;
		ItemTemplate itemTmpl = Globals.getItemRelatedBufferData().
				randomSelectItemTmpl(storeRoomTmpl.getItemTypeList(), minLevel, maxLevel);

		return new Good(itemTmpl.getId(), finalGoodNum, discount, true, this.shop);
	}
}
