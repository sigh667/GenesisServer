package com.genesis.dataserver.serverdb.task.loadglobaldata;

import com.genesis.dataserver.serverdb.ServerDBManager;

/**
 * 加载全局数据到Redis任务的接口
 * @author yaguang.xiao
 *
 */
public interface ILoadGlobalData {

    void load(ServerDBManager dbm);
}
