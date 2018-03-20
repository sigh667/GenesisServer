package com.genesis.common.shop.template;

import com.genesis.common.core.GlobalData;
import com.genesis.core.template.IAfterTemplateReady;
import com.genesis.core.template.util.TemplateCheckUtil;

import java.util.Map;

/**
 * 检查商店刷新模板
 * @author yaguang.xiao
 *
 */
public class CheckShopRefreshTemplate implements IAfterTemplateReady {

    @Override
    public void execute() {
        Map<Integer, ShopRefreshPriceTemplate> tempMap =
                GlobalData.getTemplateService().getAll(ShopRefreshPriceTemplate.class);
        TemplateCheckUtil.isSequenceTemplate(tempMap, 1, tempMap.size(), "商店刷新消耗表格配置必须连续");
    }

}
