package com.mokylin.bleach.core.util;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.apache.commons.lang3.reflect.TypeUtils;

/**
 * 泛型工具类
 * @author yaguang.xiao
 *
 */
public class GenericityUtil {

	/**
	 * 抽取指定接口上的第一个泛型类型
	 * @param fromClass
	 * @param toClass
	 * @return
	 */
	public static Class<?> extractFirstGenericType(Class<?> fromClass, Class<?> toClass) {
		TypeVariable<?> key = toClass.getTypeParameters()[0];
		Map<TypeVariable<?>, Type> typeMap = TypeUtils.getTypeArguments(
				fromClass, toClass);
		return (Class<?>) typeMap.get(key);
	}
	
}
