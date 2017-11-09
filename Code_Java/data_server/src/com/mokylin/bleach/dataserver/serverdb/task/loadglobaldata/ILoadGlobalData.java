package com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata;

import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;

/**
 * 加载全局数据到Redis任务的接口
 * @author yaguang.xiao
 *
 */
public interface ILoadGlobalData {

	void load(ServerDBManager dbm);
}
