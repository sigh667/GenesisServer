package com.mokylin.bleach.dblog.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志数据表的标识，用于GM后台的显示
 * 
 * @author yaguang.xiao
 *
 */

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TableView {

	String value() default "";

}
