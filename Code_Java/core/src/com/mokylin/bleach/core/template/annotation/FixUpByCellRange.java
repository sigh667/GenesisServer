package com.mokylin.bleach.core.template.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注一个方法，该方法会在标注了@ExcelRowBinding的对象 从Excel里加载相应列，并把加载的列作为字符串数组参数来调用相应方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FixUpByCellRange {
	int start();

	int len();
}
