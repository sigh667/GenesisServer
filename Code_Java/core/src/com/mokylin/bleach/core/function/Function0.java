package com.mokylin.bleach.core.function;

/**
 * 用于模拟无参数且返回值为R的函数的接口。<p>
 * 
 * 当一个方法需要一个函数作为参数的时候，受限于Java的能力，使用该接口创建对象来模拟实际的函数传递。<p>
 * 
 * 例：<br>
 * <pre>
 * public R exec( () => R );
 * 等价于：
 * public R exec(Function0<R> function);
 * </pre>
 * 
 * @author pangchong
 *
 * @param <R>
 */
public interface Function0<R>{

	R apply();
}
