package com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata;

import java.util.List;
import java.util.Set;

import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.bleach.dataserver.serverdb.ServerDBManager;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;

public class GlobalDataLoader {

	/** 加载全局数据任务类所在的包名 */
	private static final String PACAKGE = "com.mokylin.bleach.dataserver.serverdb.task.loadglobaldata.loaders";
	
	/**
	 * 加载全局数据
	 * 
	 * @param dbm
	 */
	public static void loadGlobalData(ServerDBManager dbm) {
		Set<Class<?>> classSet = PackageUtil.getSubClass(
				PACAKGE, ILoadGlobalData.class);
		
		try {
			for (Class<?> clazz : classSet) {
				((ILoadGlobalData) clazz.newInstance()).load(dbm);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加载与玩家相关的全局数据
	 * @param dbm
	 * @param humanList
	 */
	public static void loadHumanRelatedGlobalData(ServerDBManager dbm, List<HumanEntity> humanList) {
		Set<Class<?>> classSet = PackageUtil.getSubClass(
				PACAKGE, ILoadHumanRelatedGlobalData.class);
		
		try {
			for (Class<?> clazz : classSet) {
				((ILoadHumanRelatedGlobalData) clazz.newInstance()).load(dbm, humanList);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
