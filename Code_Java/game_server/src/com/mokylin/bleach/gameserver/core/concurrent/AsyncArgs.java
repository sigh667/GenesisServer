package com.mokylin.bleach.gameserver.core.concurrent;

import java.util.HashMap;

public class AsyncArgs {

	private final HashMap<Object, Object> map = new HashMap<>();
	
	public void add(Object key, Object value) {
		map.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <V> V get(Object key, Class<V> type){
		return (V)map.get(key);
	}
	
	public Object get(Object key){
		return map.get(key);
	}
}
