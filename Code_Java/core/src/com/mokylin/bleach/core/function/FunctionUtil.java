package com.mokylin.bleach.core.function;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.core.function.exception.NotAFunctionException;

public final class FunctionUtil {

	/**
	 * 断言类class是一个函数类。<p>
	 * 
	 * 函数类是指一个类及其超类中没有实例字段和类字段的类。函数类的主要作用在
	 * 仅仅处理数据的场合，在这种情况下不能够对数据处理进行上下文的保存。<p>
	 * 
	 * 该方法不会将所有的字段都视为不允许，final的基本类型和String类型，以及log4j
	 * 的{@link Logger}类型都是允许的。
	 * 
	 * @param clazz
	 */
	public static void assertIsFunctionClass(Class<?> clazz) {
		if(clazz == null) return;
		Field[] allField = clazz.getDeclaredFields();
		if(allField.length == 0) return;
		for(Field each: allField){
			if(Modifier.isFinal(each.getModifiers())){
				if(each.getType().isPrimitive() || each.getType() == String.class) continue;
			}
			
			Class<?> type = each.getType();
			if(!type.isAssignableFrom(Logger.class) && !type.isAssignableFrom(GeneratedMessage.class)){
				throw new NotAFunctionException(clazz.getName() + " is not a Function class, " + each.getName() + " field can be changed!");
			}
		}
		assertIsFunctionClass(clazz.getSuperclass());
	}
}
