package com.mokylin.bleach.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表明指定类型、方法或者变量被某个锁保护
 * 
 * @author yaguang.xiao
 *
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface GuardedBy {
	
	String value() default "";
	
}
