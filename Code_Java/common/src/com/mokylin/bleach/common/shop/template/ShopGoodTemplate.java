package com.mokylin.bleach.common.shop.template;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.template.annotation.ExcelRowBinding;
import com.mokylin.bleach.core.template.exception.TemplateConfigException;

/**
 * 商店获取模板
 * 
 * @author yaguang.xiao
 * 
 */
@ExcelRowBinding
public class ShopGoodTemplate extends ShopGoodTemplateVO {
	
	@Override
	public void patchUp() throws Exception {
		super.patchUp();
	}

	@Override
	public void check() throws TemplateConfigException {
		if (this.sellPosition < 0) {
			throw new TemplateConfigException(this.getSheetName(),
					this.getId(), String.format("出售位置【%d】必须大于等于0", this.sellPosition));
		}

		if (GlobalData.getTemplateService().get(this.storeRoomId,
				ShopItemStoreRoomTemplate.class) == null) {
			throw new TemplateConfigException(this.getSheetName(),
					this.getId(), String.format("库Id【%d】无效，没有在物品库中找到该库Id", this.storeRoomId));
		}

		if (this.baseNum <= 0) {
			throw new TemplateConfigException(this.getSheetName(),
					this.getId(), String.format("基础数量【%d】必须大于0", this.baseNum));
		}
		
		if (this.critRate <= 0) {
			throw new TemplateConfigException(this.getSheetName(),
					this.getId(), String.format("暴击倍率【%d】必须大于0", this.critRate));
		}
		
		if (this.critChance < 0) {
			throw new TemplateConfigException(this.getSheetName(),
					this.getId(), String.format("暴击概率【%d】必须大于等于0", this.critChance));
		}
	}
}
