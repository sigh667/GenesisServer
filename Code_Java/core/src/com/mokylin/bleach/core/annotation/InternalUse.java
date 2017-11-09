package com.mokylin.bleach.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内部使用的方法或者类，写上层逻辑的人不能使用此注解注释的方法或者类
 * @author yaguang.xiao
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE, ElementType.METHOD})
public @interface InternalUse {

}
