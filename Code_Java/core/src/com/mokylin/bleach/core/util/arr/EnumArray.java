package com.mokylin.bleach.core.util.arr;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 对象数组
 * @author yaguang.xiao
 *
 * @param <K> 键枚举类型
 * @param <V> 值类型
 */
public class EnumArray<K extends Enum<K>, V> {

	private final Map<K, V> values;
	private final Map<K, Boolean> bitSet;
	
	/**
	 * 构造对象数组
	 * @param valueClass	值类型
	 */
	private EnumArray(Class<K> keyClass, Class<V> valueClass, V defaultValue) {
		values = Maps.newEnumMap(keyClass);
		bitSet = Maps.newEnumMap(keyClass);
		
		// 初始化默认值
		for(K k : keyClass.getEnumConstants()) {
			set(k, defaultValue);
		}
	}
	
	public static <K extends Enum<K>, V> EnumArray<K, V> create(Class<K> keyClass, Class<V> valueClass, V defaultValue) {
		return new EnumArray<K, V>(keyClass, valueClass, defaultValue);
	}
	
	/**
	 * 获取指定Key的属性
	 * @param key
	 * @return
	 */
	public V get(K key) {
		return this.values.get(key);
	}
	
	/**
	 * 设置指定下标的属性
	 * @param key
	 */
	public void set(K key, V val) {
		V old = this.values.get(key);
		
		if((old == null && val != null) || (old != null && !old.equals(val))) {
			this.values.put(key, val);
			this.bitSet.put(key, true);
		}
	}
	
	public int size() {
		return this.values.size();
	}
	
	public boolean isChanged() {
		return this.bitSet.size() > 0;
	}
	
	/**
	 * 获取数组中改变的值，本方法内会把改变标记全部清掉
	 * @return
	 */
	public List<KeyValuePair<K, V>> getChanged() {
		List<KeyValuePair<K, V>> changed = Lists.newLinkedList();
		
		for(Entry<K, Boolean> entry : this.bitSet.entrySet()) {
			K key = entry.getKey();
			Boolean isChange = entry.getValue();
			
			if(isChange != null && isChange) {
				changed.add(KeyValuePair.create(key, this.values.get(key)));
			}
		}
		
		this.bitSet.clear();
		
		return changed;
	}

	public Collection<V> values() {
		return values.values();
	}
}
