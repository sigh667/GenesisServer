package com.mokylin.bleach.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不可变对象，表示某个类的对象时不可变的
 * 
 * @author yaguang.xiao
 *
 */

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(value={ElementType.TYPE})
public @interface Immutable {

}
