package com.mokylin.bleach.core.enums;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.util.Assert;

/**
 * 数组形式的IndexedEnum
 * @author baoliang.shen
 *
 * @param <E>
 */
public interface ArrayIndexedEnum<E extends Enum<?>> {

	/**
	 * 获取该枚举的索引值
	 * 
	 * @return 返回>=0的索引值
	 */
	public abstract int getIndex();
	

	public static class EnumUtil {
		
		/** 索引警戒上限，发现超过此值的索引可能存在较大的空间浪费*/
		private static final int WORNNING_MAX_INDEX = 1000;
		
		/**
		 * 将枚举中的元素放到一个List中，每个元素在list中的下表即为他的index，如果有不连续的index，则空缺的index用null填充
		 * 
		 * @param <E>
		 * @param enums
		 * @return
		 */
		public static <E extends ArrayIndexedEnum<?>> List<E> toIndexes(E[] enums) {
			int maxIndex = Integer.MIN_VALUE;
			int curIdx = 0;
			// 找到最大index，此值+1就是结果list的size
			for (E enm : enums) {
				curIdx = enm.getIndex();
				// 索引不能为负
				Assert.isTrue(curIdx >= 0, String.format("枚举索引不能为负 index: %1$d type: %2$s ", curIdx, enums.getClass()
						.getComponentType().getName()));
				if (curIdx > maxIndex) {
					maxIndex = curIdx;
				}
			}
			if (maxIndex >= WORNNING_MAX_INDEX) {
				/** 日志 */
				Logger logger = LoggerFactory.getLogger(ArrayIndexedEnum.class);
				logger.warn(String.format("警告：枚举类%s中有索引超过%d的索引，如果有很多索引空缺，可能会造成空间浪费", enums.getClass()
						.getComponentType().getName(), WORNNING_MAX_INDEX));
			}
			List<E> instances = new ArrayList<E>(maxIndex + 1);
			// 先全用null填充
			for (int i = 0; i < maxIndex + 1; i++) {
				instances.add(null);
			}
			for (E enm : enums) {
				curIdx = enm.getIndex();
				// 索引必须唯一
				Assert.isTrue(instances.get(curIdx) == null, "枚举中有重复的index type= "
						+ enums.getClass().getComponentType().getName());
				instances.set(curIdx, enm);
			}
			return instances;
		}
		
		/**
		 * 根据枚举index返回枚举元素，index从0开始
		 * 
		 * @param <T>
		 *            枚举类型
		 * @param values
		 *            枚举元素输注
		 * @param index
		 *            从0开始的index
		 * @return 枚举元素
		 */
		public static <T extends Enum<T>> T valueOf(List<T> values, int index) {
			if (index<0 || index>=values.size()) {
				return null;
			} else {
				return values.get(index);
			}
		}
	}
}
