package com.mokylin.bleach.core.msgfunc.protobufutil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Parser;
import com.mokylin.bleach.protobuf.MessageType;

public class MsgUtil {
	
	@SuppressWarnings("rawtypes")
	public static Parser extractMsgParser(Class<?> msgTypeClass)
			throws NoSuchFieldException, IllegalAccessException {
		Field parserField = msgTypeClass.getDeclaredField("PARSER");
		Parser parser = (Parser) (parserField.get(null));
		if (parser == null)
			throw new RuntimeException("Class[" + msgTypeClass
					+ "] 的解析器为null！可能消息类不正确");
		return parser;
	}

	/**
	 * 根据MsgClass从MessageOption中获取MessageType。
	 * 
	 * @param msgClass
	 * @param isCG
	 * @return
	 */
	public static int getMsgType(Class<?> msgClass, boolean isCG) {
		try {
			Method m = msgClass.getDeclaredMethod("getDescriptor");
			m.setAccessible(true);
			Descriptor d = (Descriptor) m.invoke(null);
			if (isCG) {
				return d.getOptions().getExtension(MessageType.cgMessageType)
						.getNumber();
			} else {
				return d.getOptions().getExtension(MessageType.gcMessageType)
						.getNumber();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
