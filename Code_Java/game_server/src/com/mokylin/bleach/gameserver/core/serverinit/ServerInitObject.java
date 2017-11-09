package com.mokylin.bleach.gameserver.core.serverinit;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * 游戏逻辑服务器启动初始化的中间对象。<p>
 * 
 * 该对象在逻辑服务器启动时的逻辑服务器启动线程中创建，为中间变量，
 * 目的是在加载数据后创建各个公共Actor使用。
 * 
 * @author pangchong
 *
 */
public final class ServerInitObject {

	private ImmutableMap<Class<?>, Object> map; 
	
	public ServerInitObject(Map<Class<?>, Object> map){
		checkArgument(map!=null && !map.isEmpty(), "");
		this.map = ImmutableMap.copyOf(map);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Optional<T> get(Class<T> clazz){
		return Optional.fromNullable((T)map.get(clazz));
	}
}
