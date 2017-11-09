package com.mokylin.bleach.dipserver.core.handler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http处理器注解
 * @author yaguang.xiao
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface HttpHandler {

	/**
	 * URI路径
	 * @return
	 */
	String path() default "";
	
}
