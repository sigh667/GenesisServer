package com.genesis.gamedb.human;

import com.genesis.gamedb.orm.entity.FunctionEntity;
import com.genesis.gamedb.orm.entity.HeroEntity;
import com.genesis.gamedb.orm.entity.HumanEntity;
import com.genesis.gamedb.orm.entity.ItemEntity;
import com.genesis.gamedb.orm.entity.ShopEntity;

import java.util.List;

/**
 * 角色详细数据，用于human对象的加载
 * @author Joey
 *
 */
public class HumanData {

    /**服务器ID*/
    public volatile int serverId;
    /**角色ID*/
    public volatile Long humanId;

    /**角色*/
    public volatile HumanEntity humanEntity;
    /**道具*/
    public volatile List<ItemEntity> itemEntityList;
    /**英雄*/
    public List<HeroEntity> heroEntityList;
    /** 商店 */
    public List<ShopEntity> shopEntityList;
    /** 功能开启实体 */
    public List<FunctionEntity> functionEntityList;

    public HumanData(int serverId, Long humanId) {
        this.serverId = serverId;
        this.humanId = humanId;
    }
}
