package com.mokylin.bleach.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示某种类型是线程安全的
 * 
 * @author yaguang.xiao
 *
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE})
public @interface ThreadSafe {

}
