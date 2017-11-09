package com.mokylin.bleach.gamedb.human;

import java.util.List;

import com.mokylin.bleach.gamedb.orm.entity.FunctionEntity;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gamedb.orm.entity.ItemEntity;
import com.mokylin.bleach.gamedb.orm.entity.HeroEntity;
import com.mokylin.bleach.gamedb.orm.entity.ShopEntity;

/**
 * 角色详细数据，用于human对象的加载
 * @author baoliang.shen
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
