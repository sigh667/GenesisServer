package com.genesis.dataserver.serverdb.task.loadglobaldata;

import com.genesis.dataserver.serverdb.ServerDBManager;
import com.genesis.gamedb.orm.entity.HumanEntity;

import java.util.List;

/**
 * 加载玩家相关的全局数据任务
 * @author yaguang.xiao
 *
 */
public interface ILoadHumanRelatedGlobalData {

    void load(ServerDBManager dbm, List<HumanEntity> humanList);
}
