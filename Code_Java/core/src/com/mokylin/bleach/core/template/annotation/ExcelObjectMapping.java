package com.mokylin.bleach.core.template.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识类中的属性为集合属性，包括Map、Set、List等三类
 * <p>
 * 用于根据配置读出集合中的对象
 * 
 * <pre>
 * @ExcelObjectMapping(fieldsNumber = "1,2,3")
 * 上面示例用于读取从表格读取数据赋值到对象中,根据分号进行分隔每个对象的数据
 * </pre>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
public @interface ExcelObjectMapping {

	/**
	 * excel中的列编号串
	 */
	String fieldsNumber();
}
