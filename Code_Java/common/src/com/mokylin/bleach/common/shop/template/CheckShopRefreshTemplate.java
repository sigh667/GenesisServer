package com.mokylin.bleach.common.shop.template;

import java.util.Map;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.core.template.IAfterTemplateReady;
import com.mokylin.bleach.core.template.util.TemplateCheckUtil;

/**
 * 检查商店刷新模板
 * @author yaguang.xiao
 *
 */
public class CheckShopRefreshTemplate implements IAfterTemplateReady {

	@Override
	public void execute() {
		Map<Integer, ShopRefreshPriceTemplate> tempMap = GlobalData.getTemplateService().getAll(ShopRefreshPriceTemplate.class);
		TemplateCheckUtil.isSequenceTemplate(tempMap, 1, tempMap.size(), "商店刷新消耗表格配置必须连续");
	}

}
