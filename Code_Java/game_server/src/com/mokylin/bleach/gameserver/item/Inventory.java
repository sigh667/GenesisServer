package com.mokylin.bleach.gameserver.item;

import java.util.List;

import com.google.common.base.Optional;
import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.item.ItemContainer;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.gamedb.orm.entity.ItemEntity;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.item.ItemInfo.DetailItemInfo;
import com.mokylin.bleach.protobuf.ItemMessage.GCInventoryInfo;
import com.mokylin.bleach.protobuf.ItemMessage.ItemData;
import com.mokylin.bleach.protobuf.ItemMessage.ItemData.Builder;

/**
 * 背包
 * @author baoliang.shen
 *
 */
public class Inventory extends ItemContainer<Human, Item> {
	
	public Inventory(Human human) {
		super(human);
	}

	public void loadFromEntity(List<ItemEntity> itemEntityList) {
		if (itemEntityList==null)
			return;
		for (ItemEntity itemEntity : itemEntityList) {
			Item item = Item.buildFromItemEntity(itemEntity, human);
			map.put(item.getTemplateId(), item);
			if(item.getOverlap() <= 0){
				throw new RuntimeException("Player: ["+human.getId()+"] has Item: ["+item.getTemplateId()+"], but the amount is 0!");
			}
		}
	}

	/**
	 * 根据物品Template ID获取物品的信息。
	 * 
	 * @param itemTemplateId
	 * @return
	 */
	public Optional<DetailItemInfo> getItemInfo(int itemTemplateId) {
		Item item = map.get(itemTemplateId);
		if(item == null) return Optional.absent();
		return Optional.of(new DetailItemInfo(item));
	}

	/**
	 * 登录时，给客户端发背包所有信息
	 */
	public void notifyOnLogin() {
		com.mokylin.bleach.protobuf.ItemMessage.GCInventoryInfo.Builder inventoryBuilder = GCInventoryInfo.newBuilder();
		Builder itemBuilder = ItemData.newBuilder();
		for (Item item : map.values()) {
			itemBuilder.clear();
			itemBuilder.setOverlap(item.getOverlap());
			itemBuilder.setTemplateId(item.getTemplateId());
			
			inventoryBuilder.addItems(itemBuilder.build());
		}
		GCInventoryInfo msg = inventoryBuilder.build();
		
		human.sendMessage(msg);
	}

	/**
	 * 道具使用
	 * 
	 * @param itemTemplateId	道具的TemplateID
	 * @param amount	道具数量
	 */
	public void itemUse(int itemTemplateId, int amount) {
		if (amount<=0)
			return;
		Item item = map.get(itemTemplateId);
		
		if(item == null) return;
		
		if (item.getOverlap() < amount)
			return;
		
		//暂时先直接扣数量
		item.setOverlap(item.getOverlap() - amount);
		if(item.getOverlap() > 0){
			item.setModified();
		}else{
			map.remove(itemTemplateId);
			item.onDelete();
		}		
		//不用给客户端回消息
	}
	
	/**
	 * 增加物品。<p>
	 * 
	 * 增加成功，返回true；否则，在以下情况下增加失败，返回false:<br>
	 * <li>增加的物品数量不为正整数；</li>
	 * <li>增加的物品TemplateId不存在；</li>
	 * <br><br>
	 * 
	 * 另外，当物品已经达到策划配置的堆叠上限时，该方法仅仅将物品的数量设置为上限，多出的将被丢弃。
	 * 
	 * @param itemTemplateId
	 * @param amount
	 */
	public boolean addItem(int itemTemplateId, int amount){
		if(amount<=0) return false;
		
		Item item = map.get(itemTemplateId);
		if(item == null){
			ItemTemplate itemTmpl = GlobalData.getTemplateService().get(itemTemplateId, ItemTemplate.class);
			if(itemTmpl == null) return false;
			item = Item.buildFromTemplateId(itemTemplateId, amount, human);
			map.put(itemTemplateId, item);
		}else{
			if(item.getOverlap() <= item.getTemplate().getMaxOverlap() - amount){
				item.setOverlap(item.getOverlap() + amount);
			}else{
				item.setOverlap(item.getTemplate().getMaxOverlap());
			}
		}
		item.setModified();
		return true;
	}
	
	/**
	 * 删除物品。<p>
	 * 
	 * 删除成功，返回true；否则，以下情况返回false:<br>
	 * <li>物品不存在；</li>
	 * <li>要删除的数量不为正整数；</li>
	 * <li>要删除的数量多于当前的物品数量；</li>
	 * 
	 * @param itemTemplateId
	 * @param amount
	 * @return
	 */
	public boolean deleteItem(int itemTemplateId, int amount) {
		Item item = map.get(itemTemplateId);
		if(item == null) return false;
		if(amount <= 0 || amount > item.getOverlap()) return false;
		
		item.setOverlap(item.getOverlap() - amount);
		if(item.getOverlap() > 0){
			item.setModified();
		}else{
			map.remove(itemTemplateId);
			item.onDelete();
		}
		return true;
	}
	
	/**
	 * 批量删除物品。<p>
	 * 
	 * 该方法使用需要注意一下情况：<br>
	 * <li>首先，该方法需要删除至少两个物品，如果只删除一个，该方法会返回false，麻烦使用deleteItem方法；</li>
	 * <li>该方法会保证要么整体删除，要么一个都不删除，不删除的原因包括：参数不正确，物品不存在，物品数量不够；</li>
	 * 
	 * @param items
	 * @return
	 */
	public boolean batchDeleteItems(ItemInfo... items){
		
		if(items == null || items.length < 2) return false;
		
		Item[] allItems = new Item[items.length];
		int[] counts = new int[items.length];
		
		for(int i = 0; i<allItems.length; i++){
			ItemInfo temp = items[i];
			
			if(temp == null) return false;
			
			Item item = this.map.get(temp.itemTemplateId);
			if(item == null) return false;
			
			if(item.getOverlap() < temp.amount) return false;
			
			allItems[i] = item;
			counts[i] = item.getOverlap() - temp.amount;
		}
		
		for(int i = 0; i<allItems.length; i++){
			Item item = allItems[i];
			item.setOverlap(counts[i]);
			if(item.getOverlap() > 0){
				item.setModified();
			}else{
				map.remove(item.getTemplateId());
				item.onDelete();
			}
		}
		return true;
	}

}
