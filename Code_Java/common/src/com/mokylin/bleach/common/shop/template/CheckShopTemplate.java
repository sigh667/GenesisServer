package com.mokylin.bleach.common.shop.template;

import java.util.Set;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.shop.ShopType;
import com.mokylin.bleach.core.template.IAfterTemplateReady;

/**
 * 检查商店模板
 * @author yaguang.xiao
 *
 */
public class CheckShopTemplate implements IAfterTemplateReady {

	@Override
	public void execute() {
		Set<Integer> keySet = GlobalData.getTemplateService().getAll(ShopTemplate.class).keySet();
		
		for (ShopType shopType : ShopType.values()) {
			if(!keySet.contains(shopType.getIndex())) {
				throw new RuntimeException(String.format("商店类型【%s】在商店模板中找不到对应的配置", shopType));
			}
		}
	}

}
