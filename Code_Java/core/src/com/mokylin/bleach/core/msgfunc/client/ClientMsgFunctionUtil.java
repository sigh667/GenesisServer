package com.mokylin.bleach.core.msgfunc.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;
import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.core.msgfunc.exception.MessageFunctionParseException;
import com.mokylin.bleach.core.msgfunc.protobufutil.MsgUtil;
import com.mokylin.bleach.core.util.GenericityUtil;
import com.mokylin.bleach.core.util.PackageUtil;
import com.mokylin.bleach.protobuf.MessageType;
import com.mokylin.bleach.protobuf.MessageType.CGMessageType;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class ClientMsgFunctionUtil {

	/**
	 * 
	 * @param clisentMsgFuncsPackage
	 * @param isCG
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Pair<Table<MessageTarget, Integer, IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>>, Map<Integer, Parser<? extends GeneratedMessage>>>
	buildMsgFunction(String clisentMsgFuncsPackage) {
		Table<MessageTarget, Integer, IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>> funcsTable = HashBasedTable.create();
		Map<Integer, Parser<? extends GeneratedMessage>> parserMap = new HashMap<>();

		Set<Class<?>> funcClassSet = PackageUtil.getSubClass(clisentMsgFuncsPackage, IClientMsgFunc.class);

		if (funcClassSet == null || funcClassSet.isEmpty()) return Pair.of(funcsTable, parserMap);

		try {
			for (Class<?> each : funcClassSet) {
				FunctionUtil.assertIsFunctionClass(each);
				Class<?> msgTypeClass = GenericityUtil.extractFirstGenericType(each, IClientMsgFunc.class);
				Integer msgTypeId = MsgUtil.getMsgType(msgTypeClass, true);
				CGMessageType msgType = CGMessageType.valueOf(msgTypeId);
				MessageTarget target = msgType.getValueDescriptor().getOptions().getExtension(MessageType.tARGET);

				if (funcsTable.contains(target, msgTypeId))throw new RuntimeException("Multi Client Msg Function for One Message Type: " + msgTypeClass.getName());
				if (parserMap.containsKey(msgTypeId))throw new RuntimeException("Multi Client Msg Parser for One Message Type: " + msgTypeClass.getName());

				funcsTable.put(target, msgTypeId, (IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>) each.newInstance());
				parserMap.put(msgTypeId, MsgUtil.extractMsgParser(msgTypeClass));
			}
		} catch (Exception e) {
			throw new MessageFunctionParseException(e);
		}
		return Pair.of(funcsTable, parserMap);
	}

}
