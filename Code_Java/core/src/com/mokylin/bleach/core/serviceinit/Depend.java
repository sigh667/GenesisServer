package com.mokylin.bleach.core.serviceinit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来注明依赖的服务类型
 * 
 * @author yaguang.xiao
 *
 */

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Depend {

	/**
	 * 依赖的类型集合
	 * @return
	 */
	Class<? extends ServiceInitializeRequired>[] value() default {};
}
