package com.mokylin.bleach.gameserver.core.serverinit;

import com.google.common.base.Optional;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;

/**
 * 用于服务器启动的初始化函数对象接口。
 * 
 * @author pangchong
 *
 */
public abstract class ServerInitFunction<T> {
	
	/**
	 * 在逻辑服务器启动时加载服务器数据的线程池里面执行，用来加载服务器全局数据
	 * @param sGlobals
	 * @return
	 */
	public abstract T apply(ServerGlobals sGlobals);
	
	/**
	 * 此方法在ServerActor中执行
	 * @param optional
	 * @param sGlobals
	 */
	@SuppressWarnings("unchecked")
	public final void set(Optional<?> optional, ServerGlobals sGlobals) {
		this.set((T) optional.get(), sGlobals);
	}
	
	/**
	 * 此方法在ServerActor中执行，用来对服务器全局数据赋值
	 * @param result
	 * @param sGlobals
	 */
	protected abstract void set(T result, ServerGlobals sGlobals);
}
