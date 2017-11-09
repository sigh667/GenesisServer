package com.mokylin.bleach.dblog.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于支持GM后台显示的排序、查找功能
 * 
 * @author yaguang.xiao
 *
 */

@Target({ java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnView {

	String value() default "";

	/**
	 * 搜索条件：=、>=、>等
	 */
	String search() default "";

	/**
	 * @return 升序或者降序 sql
	 */
	boolean orderBy() default false;

	/**
	 * 需要用下拉条显示的情况下所有选项名称
	 * @return
	 */
	String[] optionName() default {};

	/**
	 * 选项名称对应的数值
	 * @return
	 */
	int[] optionValue() default {};

	boolean time() default false;

}
