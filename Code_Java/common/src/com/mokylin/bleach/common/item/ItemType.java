package com.mokylin.bleach.common.item;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 道具类型
 */
public enum ItemType {
	ActiveConsumables,
	PassiveConsumables,
	EquipmentPieces,
	EquipmentMaterialPieces,
	Equipment,
	EquipmentMaterial,
	Soulstone,
	;
	
	private final int index;
	
	ItemType() {
		this.index = 1 << this.ordinal();
	}
	
	public int getIndex() {
		return this.index;
	}
	
	/** 最大枚举数量 */
	private static final int MAX_ENUM_NUM = 31;
	/** 最大物品类型Id */
	private static int MAX_ITEM_TYPE_ID;
	static {
		int enumNum = ItemType.values().length;
		if(enumNum > MAX_ENUM_NUM) {
			throw new RuntimeException(String.format(
					"ItemType枚举数量【%d】超过最大数量【%d】", enumNum, MAX_ENUM_NUM));
		}
		
		MAX_ITEM_TYPE_ID = (int) (Math.pow(2, enumNum) - 1);
	}
	
	/**
	 * 根据物品类型Id来获取指定的枚举列表
	 * @param itemTypeId
	 * @return
	 */
	public static List<ItemType> getByItemTypeId(int itemTypeId) {
		List<ItemType> typeList = Lists.newLinkedList();
		for(ItemType itemType : ItemType.values()) {
			int index = itemType.getIndex();
			if((index & itemTypeId) != 0) {
				typeList.add(itemType);
			}
		}
		
		return typeList;
	}
	
	/**
	 * 判断物品类型Id是否有效
	 * @param itemTypeId
	 * @return
	 */
	public static boolean isValid(int itemTypeId) {
		return itemTypeId >= 1 && itemTypeId <= MAX_ITEM_TYPE_ID;
	}
	
}
