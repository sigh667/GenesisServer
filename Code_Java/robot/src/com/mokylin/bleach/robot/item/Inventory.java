package com.mokylin.bleach.robot.item;

import java.util.ArrayList;
import java.util.List;

import com.mokylin.bleach.common.item.ItemContainer;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.protobuf.ItemMessage.GCInventoryInfo;
import com.mokylin.bleach.protobuf.ItemMessage.ItemData;
import com.mokylin.bleach.robot.human.Human;

/**
 * 机器人背包的逻辑数据模块
 * @author baoliang.shen
 *
 */
public class Inventory extends ItemContainer<Human, Item> {

	public Inventory(Human human) {
		super(human);
	}

	public List<Item> getAll() {
		List<Item> list = new ArrayList<>();
		for (Item v : map.values()) {
			list.add(v);
		}
		return list;
	}

	/**
	 * 添加物品<p>
	 * 如果超过物品叠加上限，就抹平到上限
	 * @param itemTemplateId
	 * @param amount
	 */
	public void addItem(int itemTemplateId, int amount) {
		if(amount <= 0) {
			String error = String.format("addItem(itemTemplateId==%d, amount==%d) amount<=0!", itemTemplateId, amount);
			throw new RuntimeException(error);
		}

		Item item = map.get(itemTemplateId);
		if(item == null) {
			item = Item.buildNewItem(itemTemplateId, amount, human);
			int overlap = item.getOverlap();
			ItemTemplate template = item.getTemplate();
			if (overlap > template.getMaxOverlap()) {
				item.setOverlap(template.getMaxOverlap());
			}
			map.put(itemTemplateId, item);
		} else {
			int overlap = item.getOverlap();
			ItemTemplate template = item.getTemplate();
			if (template.getMaxOverlap() - overlap < amount) {
				item.setOverlap(template.getMaxOverlap());
			} else {
				item.setOverlap(overlap + amount);
			}
		}
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
		if(item.getOverlap() == 0){
			map.remove(itemTemplateId);
		}
		return true;
	}

	public void load(GCInventoryInfo msg) {
		List<ItemData> itemsList = msg.getItemsList();
		for (ItemData itemData : itemsList) {
			Item item = Item.buildFromItemEntity(itemData, human);
			map.put(item.getTemplateId(), item);
			if(item.getOverlap() <= 0){
				throw new RuntimeException("Player: ["+human.getUuid()+"] has Item: ["+item.getTemplateId()+"], but the amount is 0!");
			}
		}
	}
}
