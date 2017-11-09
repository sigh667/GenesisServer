package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.TemplateObject;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;
import com.mokylin.bleach.core.template.annotation.ExcelCellBinding;

/**
 * 商店货物模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ShopGoodTemplateVO extends TemplateObject {
	
	/** 商店类型 */
	@ExcelCellBinding(offset = 1)
	protected com.mokylin.bleach.common.shop.ShopType shopType;

	/** 出售位置 */
	@ExcelCellBinding(offset = 2)
	protected int sellPosition;

	/** 库Id */
	@ExcelCellBinding(offset = 3)
	protected int storeRoomId;

	/** 基础数量 */
	@ExcelCellBinding(offset = 4)
	protected int baseNum;

	/** 暴击倍率 */
	@ExcelCellBinding(offset = 5)
	protected int critRate;

	/** 暴击概率 */
	@ExcelCellBinding(offset = 6)
	protected int critChance;


	public com.mokylin.bleach.common.shop.ShopType getShopType() {
		return this.shopType;
	}

	public void setShopType(com.mokylin.bleach.common.shop.ShopType shopType) {
		if (shopType == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[商店类型]shopType不可以为空");
		}	
		this.shopType = shopType;
	}
	
	public int getSellPosition() {
		return this.sellPosition;
	}

	public void setSellPosition(int sellPosition) {
		if (sellPosition < 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[出售位置]sellPosition的值不得小于0");
		}
		this.sellPosition = sellPosition;
	}
	
	public int getStoreRoomId() {
		return this.storeRoomId;
	}

	public void setStoreRoomId(int storeRoomId) {
		if (storeRoomId == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[库Id]storeRoomId不可以为0");
		}
		this.storeRoomId = storeRoomId;
	}
	
	public int getBaseNum() {
		return this.baseNum;
	}

	public void setBaseNum(int baseNum) {
		if (baseNum == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[基础数量]baseNum不可以为0");
		}
		this.baseNum = baseNum;
	}
	
	public int getCritRate() {
		return this.critRate;
	}

	public void setCritRate(int critRate) {
		if (critRate == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[暴击倍率]critRate不可以为0");
		}
		this.critRate = critRate;
	}
	
	public int getCritChance() {
		return this.critChance;
	}

	public void setCritChance(int critChance) {
		if (critChance == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[暴击概率]critChance不可以为0");
		}
		this.critChance = critChance;
	}
	

	@Override
	public String toString() {
		return "ShopGoodTemplateVO[shopType=" + shopType + ",sellPosition=" + sellPosition + ",storeRoomId=" + storeRoomId + ",baseNum=" + baseNum + ",critRate=" + critRate + ",critChance=" + critChance + ",]";

	}
}