package com.mokylin.bleach.common.item;

import java.util.HashMap;

/**
 * 持有Item的容器类，单纯的容器操作都可以写在这里
 * @author baoliang.shen
 *
 * @param <K>	此容器的持有者，一般为Human
 * @param <V>	道具的具体实现
 */
public class ItemContainer<K,V extends IItem> {

	protected K human;

	/** TempalteId - Item */
	protected HashMap<Integer, V> map = new HashMap<>();
	
	
	public ItemContainer(K human) {
		this.human = human;
	}
	public K getHuman() {
		return human;
	}
	
	/**
	 * 根据物品Template ID判断是否存在该物品。
	 * 
	 * @param itemTemplateId
	 * @return
	 */
	public boolean containsItem(int itemTemplateId){
		return map.containsKey(itemTemplateId);
	}

	/**
	 * 根据物品Template ID获取物品数量。<p>
	 * 
	 * <b>注意，当物品不存在，返回0；当物品当前数量为0时也返回0。注意区别逻辑！</b>
	 * 
	 * @param itemTemplateId
	 * @return	物品数量，当物品不存在，返回0。
	 */
	public int getItemAmount(int itemTemplateId){
		if(!containsItem(itemTemplateId))
			return 0;
		return map.get(itemTemplateId).getOverlap();
	}
}
