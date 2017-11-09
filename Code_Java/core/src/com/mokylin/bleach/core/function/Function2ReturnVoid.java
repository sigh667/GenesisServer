package com.mokylin.bleach.core.function;

/**
 * 用于模拟参数为两个且没有返回值的函数的接口。<p>
 * 
 * 当一个方法需要一个函数作为参数的时候，受限于Java的能力，使用该接口创建对象来模拟实际的函数传递。<p>
 * 
 * 例：<br>
 * <pre>
 * public R exec( (F, I) => void );
 * 等价于：
 * public R exec(Function2ReturnVoid<F, I> function);
 * </pre>
 * 
 * 该接口等同于{@link Function2<F, I, Void>}，只不过那么写太不爽了。
 * 
 * @author pangchong
 *
 * @param <F>
 * @param <I>
 */
public interface Function2ReturnVoid<F, I> {
	void apply(F arg1, I arg2);
}
