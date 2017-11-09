package com.mokylin.bleach.gameserver.core.offandon;

import java.util.EnumMap;

public enum OffAndOnConfig {
	Inst,
	;
	
	private EnumMap<OffAndOn, Boolean> map = new EnumMap<OffAndOn, Boolean>(OffAndOn.class);

	public void init() {
		//临时代码
		map.put(OffAndOn.LOGIN, true);	//允许登录
		map.put(OffAndOn.LOGIN_FROM_LOCAL, true);	//本地数据库登录验证
		
		//TODO 从配置文件或者数据库初始化各开关
		
	}

	/**
	 * 查询开关状态
	 * @param key
	 * @return
	 */
	public boolean get(OffAndOn key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return false;
		}
	}
}
