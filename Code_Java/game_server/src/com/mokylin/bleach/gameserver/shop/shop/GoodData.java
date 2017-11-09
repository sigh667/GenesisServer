package com.mokylin.bleach.gameserver.shop.shop;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 单个货物数据
 * 
 * @author yaguang.xiao
 * 
 */
public class GoodData {

	/** 物品模板Id */
	private int itemTemplateId;
	/** 数量 */
	private int num;
	/** 打折（万分数） */
	private int discount;
	/** 是否正在出售 */
	private boolean isSelling;

	public int getItemTemplateId() {
		return itemTemplateId;
	}

	public void setItemTemplateId(int itemTemplateId) {
		this.itemTemplateId = itemTemplateId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public boolean isSelling() {
		return isSelling;
	}

	public void setSelling(boolean isSelling) {
		this.isSelling = isSelling;
	}
	
	/**
	 * 转换成Good对象
	 * @return
	 */
	Good toGood(Shop owner) {
		return new Good(this.itemTemplateId, this.num, this.discount, this.isSelling, owner);
	}
	
	/**
	 * 创建货物数据列表
	 * @param goods
	 * @return
	 */
	static List<GoodData> createGoodDataList(List<Good> goodList) {
		List<GoodData> goodDataList = Lists.newArrayList();
		if(goodList == null || goodList.isEmpty()) {
			return goodDataList;
		}
		
		for(Good good : goodList) {
			goodDataList.add(good.toGoodData());
		}
		
		return goodDataList;
	}

}
