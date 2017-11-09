package com.mokylin.bleach.gameserver.core.serverinit;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.TypeUtils;

import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.util.PackageUtil;

/**
 * 服务器初始化函数对象帮助类。该类在GameServer启动时使用，用来扫描服务器初始化的函数对象。
 * 
 * @author pangchong
 *
 */
public class ServerInitUtil {

	public static Map<Class<?>, ServerInitFunction<?>> buildServerInitFunction(){
		Set<Class<?>> funcClassSet = PackageUtil.getSubClass("com.mokylin.bleach.gameserver", ServerInitFunction.class);
		
		if(funcClassSet == null || funcClassSet.isEmpty()) return new HashMap<>(0);
		
		Map<Class<?>, ServerInitFunction<?>> map = new HashMap<>(funcClassSet.size());
		try{
			for(Class<?> each : funcClassSet){
				//判断是否仅为函数，没有上下文
				FunctionUtil.assertIsFunctionClass(each);
				map.put(extractInitObjectType(each, ServerInitFunction.class), (ServerInitFunction<?>)each.newInstance());
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		return map;
	}

	private static Class<?> extractInitObjectType(Class<?> handler, Class<?> toClass) {
		TypeVariable<?> key = toClass.getTypeParameters()[0];
		Map<TypeVariable<?>, Type> typeMap = TypeUtils.getTypeArguments(handler, toClass);
		return (Class<?>) typeMap.get(key);
	}
}
