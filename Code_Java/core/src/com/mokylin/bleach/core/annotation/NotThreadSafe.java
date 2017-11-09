package com.mokylin.bleach.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 线程不安全的实现，用此注解来标注线程不安全的实现，特别是底层代码
 * 
 * @author yaguang.xiao
 *
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface NotThreadSafe {
}
