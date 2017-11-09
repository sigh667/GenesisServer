package com.mokylin.bleach.gameserver.item;


public class ItemInfo {

	public final int itemTemplateId;
	
	public final int amount;
	
	public ItemInfo(Item item) {
		this(item.getTemplateId(), item.getOverlap());
	}
	
	public ItemInfo(int itemTemplateId, int amount){
		this.itemTemplateId = itemTemplateId;
		this.amount = amount;
	}
	
	public static ItemInfo of(int itemTemplateId, int amount){
		return new ItemInfo(itemTemplateId, amount);
	}
	
	public static class DetailItemInfo extends ItemInfo{
		public final long uuid;
		
		public DetailItemInfo(Item item){
			this(item.getTemplateId(), item.getId(), item.getOverlap());
		}
		public DetailItemInfo(int itemTemplateId, long uuid, int amount) {
			super(itemTemplateId, amount);
			this.uuid = uuid;
		} 
	}

}
