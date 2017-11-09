package com.mokylin.bleach.core.enums;

import java.util.HashMap;
import java.util.Map;

public interface HashIndexedEnum<E extends Enum<?>> {

	/**
	 * 获取该枚举的索引值
	 * 
	 * @return 返回>=0的索引值
	 */
	public abstract int getIndex();


	public static class IndexedEnumUtil {

		public static <E extends HashIndexedEnum<?>> Map<Integer, E> toIndexes(E[] enums) {

			Map<Integer, E> map = new HashMap<>();
			for (E enm : enums) {
				int key = enm.getIndex();
				map.put(key, enm);
			}

			return map;
		}
	}
}
