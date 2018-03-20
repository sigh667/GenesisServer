package com.genesis.common.shop.template;

import com.genesis.core.template.annotation.ExcelRowBinding;
import com.genesis.core.template.exception.TemplateConfigException;

/**
 * 物品打折模板
 * @author yaguang.xiao
 *
 */
@ExcelRowBinding
public class ShopDiscountTemplate extends ShopDiscountTemplateVO {

    @Override
    public void check() throws TemplateConfigException {

    }

}
