package com.mokylin.bleach.robot.item.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mokylin.bleach.common.item.ItemType;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.robot.item.Item;
import com.mokylin.bleach.robot.item.view.model.ItemToView;

public class InventoryViewUtil {
	
	/**
	 * 构建可视对象，并排好序
	 * @param items
	 * @return
	 */
	public static List<ItemToView> buildAndSortAll(List<Item> items) {
		ArrayList<ItemToView> list = new ArrayList<>();
		
		for (Item item : items) {
			ItemToView itemToView = ItemToView.build(item);
			list.add(itemToView);
		}
		Collections.sort(list);
		
		return list;
	}

	public static List<ItemToView> buildList(InventoryType inventoryType, List<ItemToView> itemToViews) {
		List<ItemToView> list = new ArrayList<>();
		
		for (ItemToView itemToView : itemToViews) {
			final ItemTemplate itemTemplate = itemToView.getItem().getTemplate();
			final ItemType itemType = itemTemplate.getItemType();
			if (inventoryType.contains(itemType)) {
				list.add(itemToView);
			}
		}
		
		return list;
	}
}
