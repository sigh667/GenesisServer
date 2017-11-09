package com.mokylin.bleach.core.msgfunc.server;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.TypeUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.exception.MessageFunctionParseException;
import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class ServerMsgFunctionUtil {

	@SuppressWarnings("unchecked")
	public static ServerMsgFunctionService buildMsgFuncs(String serverMsgFuncsPackage) {
		checkArgument(serverMsgFuncsPackage!=null && !serverMsgFuncsPackage.isEmpty());
		Set<Class<?>> funcSet = PackageUtil.getSubClass(serverMsgFuncsPackage, IServerMsgFunc.class);
		
		Table<MessageTarget, Class<?>, IServerMsgFunc<?, MsgArgs, MsgArgs>> table = HashBasedTable.create();
		
		if(funcSet == null || funcSet.isEmpty()) return new ServerMsgFunctionService(table);
		
		try{
			for(Class<?> each : funcSet){
				FunctionUtil.assertIsFunctionClass(each);
				Class<?> msgClass = extractMsgClassFromTypeParameter(each, IServerMsgFunc.class);
				IServerMsgFunc<?, MsgArgs, MsgArgs> func = (IServerMsgFunc<?, MsgArgs, MsgArgs>) each.newInstance();
				MessageTarget msgTarget = func.getTarget();
				if(msgTarget == null) throw new RuntimeException(String.format("Server message class %s don't have a target!", each.getName()));
				if(table.contains(msgTarget, msgClass)) throw new RuntimeException("Multi Server Message Func to one Server Message [" + msgClass.getName() + "]");
				table.put(msgTarget, msgClass, func);
			}
		}catch(Exception e){
			throw new MessageFunctionParseException(e);
		}
		
		return new ServerMsgFunctionService(table);
	}
	
	/**
	 * 根据消息处理的Handler从泛型中抽取具体的消息类型。
	 * @param funcs
	 * @param toClass	有且只有一个类型参数
	 * @return
	 */
	private static Class<?> extractMsgClassFromTypeParameter(Class<?> funcs, Class<?> toClass) {
		TypeVariable<?> key = toClass.getTypeParameters()[0];
		Map<TypeVariable<?>, Type> typeMap = TypeUtils.getTypeArguments(funcs, toClass);
		return (Class<?>) typeMap.get(key);
	}
	
}
