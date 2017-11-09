package com.mokylin.bleach.test.gameserver.shop;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.human.HumanPropId;
import com.mokylin.bleach.common.item.ItemType;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.common.shop.template.ShopGoodTemplate;
import com.mokylin.bleach.common.shop.template.ShopItemPriceTemplate;
import com.mokylin.bleach.common.shop.template.ShopItemStoreRoomTemplate;
import com.mokylin.bleach.common.shop.template.ShopTemplate;
import com.mokylin.bleach.core.template.TemplateService;
import com.mokylin.bleach.core.time.TimeService;
import com.mokylin.bleach.core.util.TimeUtils;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.item.ItemRelatedTemplateBufferData;
import com.mokylin.bleach.gameserver.shop.ShopRelatedTemplateBufferData;
import com.mokylin.bleach.gameserver.shop.discount.ShopDiscount;
import com.mokylin.bleach.gameserver.shop.discount.ShopDiscountService;
import com.mokylin.bleach.gameserver.shop.shop.Good;
import com.mokylin.bleach.gameserver.shop.shop.Shop;
import com.mokylin.bleach.test.dataserver.MockDataUpdater;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { Globals.class, GlobalData.class })
public class ShopTest {

	private static ItemTemplate itemTmpl;
	private static ShopItemPriceTemplate itemPriceTmpl;
	private static ShopTemplate shopTmpl;
	
	private Human human;
	
	@BeforeClass
	public static void ready_for_test(){
		itemTmpl = createItemTemplate(1);
		itemPriceTmpl = createItemPriceTemplate();
		shopTmpl = createShopTemplate();
	}
	
	private static ItemTemplate createItemTemplate(int id) {
		ItemTemplate itemTmpl = new ItemTemplate();
		itemTmpl.setId(id);
		
		return itemTmpl;
	}
	
	private static ShopItemPriceTemplate createItemPriceTemplate() {
		ShopItemPriceTemplate priceTmpl = new ShopItemPriceTemplate();
		priceTmpl.setGeneralShopCurrencyType(Currency.DIAMOND);
		priceTmpl.setGeneralShopPrice(100);
		
		try {
			priceTmpl.patchUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return priceTmpl;
	}
	
	private ShopGoodTemplate createShopGoodTemplate(int id, int position) {
		ShopGoodTemplate tmpl = new ShopGoodTemplate();
		tmpl.setId(1);
		tmpl.setSellPosition(position);
		tmpl.setShopType(ShopType.GENERAL);
		tmpl.setStoreRoomId(1);
		tmpl.setCritChance(10000);
		tmpl.setCritRate(2);
		tmpl.setBaseNum(1);
		try {
			tmpl.patchUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tmpl;
	}
	
	private ShopItemStoreRoomTemplate createStoreRoomTemplate() {
		ShopItemStoreRoomTemplate storeRoomTmpl = new ShopItemStoreRoomTemplate();
		storeRoomTmpl.setId(1);
		storeRoomTmpl.setItemLevelScope(1);
		// Soulstone
		storeRoomTmpl.setItemType(6);
		storeRoomTmpl.setNumberMultiple(1);
		
		try {
			storeRoomTmpl.patchUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return storeRoomTmpl;
	}
	
	private static ShopTemplate createShopTemplate() {
		ShopTemplate shopTemplate = new ShopTemplate();
		shopTemplate.setId(0);
		shopTemplate.setMainShopCurrency(Currency.DIAMOND);
		shopTemplate.setTempOpenDuration(60);
		try {
			shopTemplate.patchUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return shopTemplate;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void prepare_for_each_test_case(){
		
		// 模拟商店物品模板数据
		List<ShopGoodTemplate> tmpls = Lists.newLinkedList();
		tmpls.add(this.createShopGoodTemplate(1, 0));
		tmpls.add(this.createShopGoodTemplate(2, 1));
		
		// 模拟商店相关的模板缓存数据
		ShopRelatedTemplateBufferData shopRelatedTmplBufferData = mock(ShopRelatedTemplateBufferData.class);
		when(shopRelatedTmplBufferData.getShopGoodTmplList(Mockito.any(ShopType.class))).thenReturn(tmpls);
		
		// 物品相关的模板缓存数据
		ItemRelatedTemplateBufferData itemRelatedTmplBufferData = mock(ItemRelatedTemplateBufferData.class);
		when(itemRelatedTmplBufferData.
				randomSelectItemTmpl(Mockito.any(List.class), Mockito.anyInt(), Mockito.anyInt())).
				thenReturn(itemTmpl);
		
		// Globals
		PowerMockito.mockStatic(Globals.class);
		when(Globals.getShopRelatedTemplateBufferData()).
				thenReturn(shopRelatedTmplBufferData);
		when(Globals.getItemRelatedBufferData()).
				thenReturn(itemRelatedTmplBufferData);
		when(Globals.getTimeService()).thenReturn(TimeService.Inst);
		
		// 模板服务
		TemplateService templateService = mock(TemplateService.class);
		when(templateService.get(1, ShopItemStoreRoomTemplate.class)).thenReturn(createStoreRoomTemplate());
		when(templateService.get(1, ItemTemplate.class)).thenReturn(itemTmpl);
		when(templateService.get(1, ShopItemPriceTemplate.class)).thenReturn(itemPriceTmpl);
		when(templateService.get(0, ShopTemplate.class)).thenReturn(shopTmpl);
		
		// GlobalData
		PowerMockito.mockStatic(GlobalData.class);
		when(GlobalData.getTemplateService()).thenReturn(templateService);
		
		// 物品打折服务
		ShopDiscountService discountService = mock(ShopDiscountService.class);
		ShopDiscount shopDiscount = new ShopDiscount(null, 1, 1, ShopType.GENERAL, 5000, 1, 1000, System.currentTimeMillis() + TimeUtils.DAY);
		when(discountService.getShopDiscount(Mockito.any(ShopType.class))).thenReturn(shopDiscount);
		
		// ServerGlobals
		ServerGlobals sGlobals = mock(ServerGlobals.class);
		when(sGlobals.getDiscountService()).thenReturn(discountService);
		
		// Human
		human = mock(Human.class);
		when(human.getServerGlobals()).thenReturn(sGlobals);
		when(human.getDataUpdater()).thenReturn(new MockDataUpdater());
	}
	
	@Test
	public void the_good_status_should_be_correct() {
		Shop shop = new Shop(this.human, 1, ShopType.GENERAL, null);
		human.set(HumanPropId.LEVEL, 5);
		long now = Globals.getTimeService().now();
		// 生成货物
		shop.getShopRefresh().refresh(5, now);
		
		Good good = shop.getGood(0);
		assertThat(good.getNum(), is(2));
		assertThat(good.getDiscount(), is(5000));
		assertThat(good.isSelling(), is(true));
		assertThat(good.getPrice().currency, is(Currency.DIAMOND));
		assertThat(good.getPrice().price, is(200l));
		
		// 卖出货物
		good.soldOut();
		
		assertThat(good.isSelling(), is(false));
		
		assertThat(shop.getGood(0).isSelling(), is(false));
	}
	
	@Test
	public void the_shop_should_be_refreshed() {
		Shop shop = new Shop(this.human, 1, ShopType.GENERAL, null);
		human.set(HumanPropId.LEVEL, 5);
		shop.getShopRefresh().getAutoRefresh().triggerExecute(human);
		
		// 当前货物的产生时间
		long goodBornTime = shop.getGoodBornTime();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long now = Globals.getTimeService().now();
		// 自动刷新
		shop.getShopRefresh().refresh(5, now);
		
		// 商店已经自动刷新
		assertThat(shop.isRefreshed(goodBornTime), is(true));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void the_item_template_id_should_be_correct() {
		ItemRelatedTemplateBufferData bufferData = new ItemRelatedTemplateBufferData();
		
		Field itemTmplsField = null;
		try {
			itemTmplsField = ItemRelatedTemplateBufferData.class.getDeclaredField("itemTmpls");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
		Table<ItemType, Integer, List<ItemTemplate>> itemTmpls = null;
		itemTmplsField.setAccessible(true);
		try {
			itemTmpls = (Table<ItemType, Integer, List<ItemTemplate>>)itemTmplsField.get(bufferData);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		List<ItemTemplate> itemList = Lists.newLinkedList();
		itemList.add(createItemTemplate(1));
		itemList.add(createItemTemplate(2));
		itemList.add(createItemTemplate(3));
		itemList.add(createItemTemplate(4));
		itemTmpls.put(ItemType.Equipment, 5, itemList);
		
		itemList = Lists.newLinkedList();
		itemList.add(createItemTemplate(5));
		itemList.add(createItemTemplate(6));
		itemList.add(createItemTemplate(7));
		itemList.add(createItemTemplate(8));
		itemTmpls.put(ItemType.EquipmentMaterial, 5, itemList);
		
		ItemTemplate itemTmpl = bufferData.randomSelectItemTmpl(Lists.newArrayList(ItemType.Equipment, ItemType.EquipmentMaterial), 5, 5);
		
		assertThat(itemTmpl.getId(), greaterThan(0));
		assertThat(itemTmpl.getId(), lessThan(9));
	}

}
