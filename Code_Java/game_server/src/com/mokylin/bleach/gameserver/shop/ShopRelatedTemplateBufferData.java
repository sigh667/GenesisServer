package com.mokylin.bleach.gameserver.shop;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalTime;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.common.shop.template.ShopGoodTemplate;
import com.mokylin.bleach.core.template.TemplateService;

/**
 * 商店相关的木耙缓冲数据
 * @author yaguang.xiao
 *
 */
public class ShopRelatedTemplateBufferData {

	/** 商店货物<商店类型, 商店货物模板> */
	private ListMultimap<ShopType, ShopGoodTemplate> shopGoods = ArrayListMultimap.create();
	/** 商店自动刷新时间<商店类型, 自动刷新时间> */
	private ListMultimap<ShopType, LocalTime> shopAutoRefreshTime = ArrayListMultimap.create();
	
	/**
	 * 初始化缓冲数据
	 * @param templateservice
	 */
	public void init(TemplateService templateservice) {
		this.initAndCheckShopGoods(templateservice);
		this.initShopAutoRefreshTime(templateservice);
	}
	
	/**
	 * 初始化商店自动刷新时间，并且进行排序
	 * @param templateservice
	 */
	private void initShopAutoRefreshTime(TemplateService templateservice) {
		ListMultimap<ShopType, LocalTime> shopAutoRefreshTime = ArrayListMultimap.create();
		
		for(ShopType shopType : ShopType.values()) {
			for (LocalTime refreshTime : shopType.getAutoRefreshTimeList()) {
				shopAutoRefreshTime.put(shopType, refreshTime);
			}
		}
		
		for(ShopType shopType : ShopType.values()) {
			List<LocalTime> autoRefreshTimes = shopAutoRefreshTime.get(shopType);
			if(!autoRefreshTimes.isEmpty()) {
				Collections.sort(autoRefreshTimes);
				for(LocalTime autoRefreshTime : autoRefreshTimes) {
					this.shopAutoRefreshTime.put(shopType, autoRefreshTime);
				}
			}
		}
	}
	
	/**
	 * 初始化并检查商店货物缓冲数据
	 * @param templateservice
	 */
	private void initAndCheckShopGoods(TemplateService templateservice) {
		Map<Integer, ShopGoodTemplate> shopGoodsTemplates = templateservice.getAll(ShopGoodTemplate.class);
		for(ShopGoodTemplate tmpl : shopGoodsTemplates.values()) {
			if(tmpl == null) {
				continue;
			}
			
			shopGoods.put(tmpl.getShopType(), tmpl);
		}
		
		Map<Integer, ShopGoodTemplate> shopGoodMap = Maps.newHashMap();
		for(ShopType shopType : ShopType.values()) {
			shopGoodMap.clear();
			List<ShopGoodTemplate> tmplList = this.getShopGoodTmplList(shopType);
			for(ShopGoodTemplate tmpl : tmplList) {
				shopGoodMap.put(tmpl.getSellPosition(), tmpl);
			}
			
			int posNum = tmplList.size();
			for(int i = 0;i < posNum;i ++) {
				if(!shopGoodMap.containsKey(i)) {
					throw new RuntimeException(String.format("商店货物表中【%s】商店出售位置配置有问题！", shopType.toString()));
				}
			}
		}
	}
	
	/**
	 * 获取指定商店的货物模板
	 * @param shopType
	 * @return
	 */
	public List<ShopGoodTemplate> getShopGoodTmplList(ShopType shopType) {
		return this.shopGoods.get(shopType);
	}
	
	/**
	 * 获取指定商店的刷新时间列表
	 * @param shopType
	 * @return
	 */
	public List<LocalTime> getAutoRefreshTime(ShopType shopType) {
		return this.shopAutoRefreshTime.get(shopType);
	}
	
}
