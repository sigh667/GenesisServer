package com.genesis.gameserver.shop.shop;

import com.google.common.collect.Lists;

import com.genesis.common.core.GlobalData;
import com.genesis.common.currency.Price;
import com.genesis.common.item.template.ItemTemplate;
import com.genesis.protobuf.ShopMessage.GoodInfo;

import java.util.List;

/**
 * 货物（逻辑对象）
 *
 * @author yaguang.xiao
 *
 */
public class Good {

    /** 物品模板 */
    private final ItemTemplate itemTemplate;
    /** 货物数量 */
    private final int num;
    /** 折扣 */
    private final int discount;
    /** 价格 */
    private final Price price;
    /** 所属商店 */
    private final Shop owner;
    /** 是否正在出售 */
    private boolean isSelling;

    public Good(int itemTemplateId, int num, int discount, boolean isSelling, Shop owner) {
        this.itemTemplate = GlobalData.getTemplateService().get(itemTemplateId, ItemTemplate.class);
        this.num = num;
        this.discount = discount;
        this.isSelling = isSelling;
        this.price = owner.getShopType().getPrice(itemTemplateId, num);
        this.owner = owner;
    }

    /**
     * 创建货物对象列表
     * @param goodDataList
     * @return
     */
    static List<Good> createGoodList(List<GoodData> goodDataList, Shop owner) {
        List<Good> goodList = Lists.newLinkedList();
        if (goodDataList == null || goodDataList.isEmpty()) {
            return goodList;
        }

        for (GoodData goodData : goodDataList) {
            goodList.add(goodData.toGood(owner));
        }

        return goodList;
    }

    public ItemTemplate getItemTemplate() {
        return this.itemTemplate;
    }

    public int getNum() {
        return num;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean isSelling() {
        return isSelling;
    }

    public void soldOut() {
        this.isSelling = false;
        this.owner.setModified();
    }

    public Price getPrice() {
        return price;
    }

    /**
     * 转换成GoodInfo对象
     * @return
     */
    public GoodInfo toGoodInfo() {
        GoodInfo.Builder goodInfoB = GoodInfo.newBuilder();
        goodInfoB.setItemTemplateId(this.itemTemplate.getId());
        goodInfoB.setNum(num);
        goodInfoB.setDiscount(discount);
        goodInfoB.setIsSelling(isSelling);
        return goodInfoB.build();
    }

    /**
     * 转换成GoodData对象
     * @return
     */
    GoodData toGoodData() {
        GoodData data = new GoodData();
        data.setItemTemplateId(this.itemTemplate.getId());
        data.setNum(num);
        data.setDiscount(discount);
        data.setSelling(isSelling);
        return data;
    }

}