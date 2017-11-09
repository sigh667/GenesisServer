package com.mokylin.bleach.core.isc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个方法被标记为&#64;ISCSync标明该方法涉及到服务器间通信，并且该方法的通信是同步的。
 * 这里的同步意味着数据的发送是同步的，或者网络的请求和响应是同步的。<p>
 * 
 * <b>不管是哪一种同步，标记有该注解的方法都不应该在对响应要求较高的地方使用。 </b>
 * 
 * @author pangchong
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface ISCSync {

}
