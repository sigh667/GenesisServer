package com.mokylin.bleach.core.function;

/**
 * 用于模拟参数为两个且带有返回值的函数的接口。<p>
 * 
 * 当一个方法需要一个函数作为参数的时候，受限于Java的能力，使用该接口创建对象来模拟实际的函数传递。<p>
 * 
 * 例：<br>
 * <pre>
 * public R exec( (F, I) => R );
 * 等价于：
 * public R exec(Function2<F, I, R> function);
 * </pre>
 * 
 * @author pangchong
 *
 * @param <F>
 * @param <I>
 * @param <R>
 */
public interface Function2<F, I, R> {

	public R apply(F arg1, I arg2);
}
