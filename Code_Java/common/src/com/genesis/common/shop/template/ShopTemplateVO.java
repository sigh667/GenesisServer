package com.genesis.common.shop.template;

import ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;
import com.genesis.core.template.TemplateObject;
import com.genesis.core.template.annotation.ExcelCellBinding;
import org.apache.commons.lang3.StringUtils;

/**
 * 商店模板
 * 
 * @author CodeGenerator, don't modify this file please.
 */

@ExcelRowBinding
public abstract class ShopTemplateVO extends TemplateObject {
	
	/** 商店名称 */
	@ExcelCellBinding(offset = 1)
	protected String shopName;

	/** 主消耗货币类型 */
	@ExcelCellBinding(offset = 2)
	protected com.genesis.common.currency.Currency mainShopCurrency;

	/** 临时开启时长（分钟） */
	@ExcelCellBinding(offset = 3)
	protected int tempOpenDuration;


	public String getShopName() {
		return this.shopName;
	}

	public void setShopName(String shopName) {
		if (StringUtils.isEmpty(shopName)) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[商店名称]shopName不可以为空");
		}
		this.shopName = shopName;
	}
	
	public com.genesis.common.currency.Currency getMainShopCurrency() {
		return this.mainShopCurrency;
	}

	public void setMainShopCurrency(com.genesis.common.currency.Currency mainShopCurrency) {
		if (mainShopCurrency == null) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[主消耗货币类型]mainShopCurrency不可以为空");
		}	
		this.mainShopCurrency = mainShopCurrency;
	}
	
	public int getTempOpenDuration() {
		return this.tempOpenDuration;
	}

	public void setTempOpenDuration(int tempOpenDuration) {
		if (tempOpenDuration == 0) {
			throw new TemplateConfigException(this.getSheetName(), this.getId(),
					0, "[临时开启时长（分钟）]tempOpenDuration不可以为0");
		}
		this.tempOpenDuration = tempOpenDuration;
	}
	

	@Override
	public String toString() {
		return "ShopTemplateVO{shopName=" + shopName + ",mainShopCurrency=" + mainShopCurrency + ",tempOpenDuration=" + tempOpenDuration + ",}";

	}
}