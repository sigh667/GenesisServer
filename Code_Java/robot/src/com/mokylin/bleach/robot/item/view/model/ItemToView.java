package com.mokylin.bleach.robot.item.view.model;

import com.mokylin.bleach.common.item.ItemType;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.robot.item.Item;

/**
 * 道具的显示对象
 * @author baoliang.shen
 *
 */
public class ItemToView implements Comparable<ItemToView>{

	private Item item;

	@Override
	public int compareTo(ItemToView o) {
		ItemTemplate thisTemplate = item.getTemplate();
		ItemTemplate oTemplate = o.item.getTemplate();
		ItemType thisType = thisTemplate.getItemType();
		ItemType oType = oTemplate.getItemType();

		//1.0比道具类型（从小到大）
		if (thisType.ordinal() < oType.ordinal()) {
			return -1;
		} else if (thisType.ordinal() > oType.ordinal()) {
			return 1;
		}

		//2.0都是碎片，比是否能合成
		if (thisType==ItemType.EquipmentMaterialPieces
				|| thisType==ItemType.EquipmentPieces) {
			boolean isCanCompound = item.isCanCompound();
			boolean oIsCanCompound = o.item.isCanCompound();
			if (isCanCompound==true || oIsCanCompound==false) {
				return -1;
			} else if (isCanCompound==false || oIsCanCompound==true) {
				return 1;
			}
		}
		
		//3.0比品质（从大到小）
		if (thisTemplate.getQualityType().ordinal() > oTemplate.getQualityType().ordinal()) {
			return -1;
		} else if (thisTemplate.getQualityType().ordinal() < oTemplate.getQualityType().ordinal()) {
			return 1;
		}
		
		//4.0比数量（从多到少）
		if (item.getOverlap() > o.item.getOverlap()) {
			return -1;
		} else if (item.getOverlap() < o.item.getOverlap()) {
			return 1;
		}
		
		//5.0比道具ID（从小到大）
		if (thisTemplate.getId() < oTemplate.getId()) {
			return -1;
		} else if (thisTemplate.getId() > oTemplate.getId()) {
			return 1;
		}

		//按照目前的设计，是不可能走到这里的，因为相同模板ID的道具在背包里只有一份
		return 0;
	}

	public static ItemToView build(Item item) {
		ItemToView itemToView = new ItemToView();
		itemToView.item = item;
		return itemToView;
	}

	@Override
	public String toString() {
		return item.getTemplate().getName();
	}

	public Object get(String columnName) {
		switch (columnName) {
		case "名称":
			return item.getTemplate().getName();
		case "叠加数量":
			return item.getOverlap();
		case "使用等级":
			return item.getTemplate().getUseLevel();
		case "品质":
			return item.getTemplate().getQualityType();

		default:
			String error = String.format("未知的道具列名：【%s】", columnName);
			throw new RuntimeException(error);
		}
	}

	public Item getItem() {
		return this.item;
	}
}
