package com.mokylin.bleach.core.util.arr;

/**
 * 键值对
 * @author yaguang.xiao
 *
 * @param <K> 
 * @param <V>
 */
public class KeyValuePair<K, V> {

	private final K key;
	private final V value;
	
	public KeyValuePair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public static <K, V> KeyValuePair<K, V> create(K key, V value) {
		return new KeyValuePair<K, V>(key, value);
	}
	
	public K getKey() {
		return this.key;
	}
	
	public V getValue() {
		return this.value;
	}
}
