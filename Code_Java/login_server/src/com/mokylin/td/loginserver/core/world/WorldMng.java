package com.mokylin.td.loginserver.core.world;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 负责管理WorldServer相关的业务
 * 
 * @author baoliang.shen
 *
 */
public class WorldMng {

	/**<世界服ID, 该世界服信息>*/
	private static Map<Integer, WorldInfo> map = Maps.newConcurrentMap();

	static{
		// 临时代码，等世界服和本服连通之后，将此代码移除 TODO
		WorldInfo info = new WorldInfo(1, null);
		info.worldStatus = WorldStatus.Ok;
		map.put(1, info);
	}

	public static WorldInfo getWorldInfo(Integer serverId) {
		return map.get(serverId);
	}
}
