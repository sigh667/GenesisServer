package com.mokylin.bleach.dblog.anno;

import java.util.LinkedHashMap;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * ColumnView 注解的检查
 * 
 * @author yaguang.xiao
 *
 */

public class ColumnViewOptionChecker {

	/**
	 * 检查ColumnView是否合法
	 * @param view
	 * @return
	 */
	public static LinkedHashMap<Integer, String> check(ColumnView view) {
		if (view == null)
			return null;

		if (view.optionName() == null && view.optionValue() == null)
			return null;

		if (view.optionName() != null && view.optionValue() != null
				&& view.optionName().length == view.optionValue().length) {
			
			if(view.optionName().length == 0)
				return null;
			
			if (view.search() != null && !"".equals(view.search()) && !"=".equals(view.search())) {
				System.out.println("view.search() = " + view.search() + "-----value = " + view.value());
				throw new RuntimeException("只有==判断能使用下拉条显示");
			}
				

			if (view.optionName().length == 0)
				return null;

			if (Sets.newHashSet(ArrayUtils.toObject(view.optionValue())).size() != view.optionValue().length)
				throw new RuntimeException("选项值重复");

			LinkedHashMap<Integer, String> map = Maps.newLinkedHashMap();
			for (int i = 0; i < view.optionName().length; i++) {
				if (view.optionName()[i] == null || map.containsKey(view.optionValue()[i]))
					throw new RuntimeException("选项名称重复");

				map.put(view.optionValue()[i], view.optionName()[i]);
			}

			return map;
		}
		
		throw new RuntimeException("选项名称和值不对应");
	}
}
