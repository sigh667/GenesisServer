package com.mokylin.bleach.core.template.util;

import java.util.Map;

import com.mokylin.bleach.core.template.TemplateObject;

public class TemplateCheckUtil {

	/**
	 * 校验模板配置是否连续递加，校验闭区间
	 * 
	 * @param templateMap
	 *            模板集合
	 * @param startIndex
	 *            起始Index
	 * @param endIndex
	 *            结束Index
	 * @param errorMsg
	 *            模板配置不连续时，抛出的异常
	 * 
	 * @throws RuntimeException
	 *             (errorMsg + " ErrorIndex:" + i)
	 */
	public static void isSequenceTemplate(
			Map<Integer, ? extends TemplateObject> templateMap, int startIndex,
			int endIndex, String errorMsg) {
		for (int i = startIndex; i <= endIndex; ++i) {
			if (!templateMap.containsKey(i)) {
				throw new RuntimeException(errorMsg + " ErrorIndex:" + i);
			}
		}
	}
}
