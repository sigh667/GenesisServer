package com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata;

import java.util.List;

import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;

/**
 * 加载玩家相关的全局数据任务
 * @author yaguang.xiao
 *
 */
public interface ILoadHumanRelatedGlobalData {

	void load(ServerDBManager dbm, List<HumanEntity> humanList);
}
