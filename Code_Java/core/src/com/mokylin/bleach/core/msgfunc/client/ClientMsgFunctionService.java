package com.mokylin.bleach.core.msgfunc.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.mokylin.bleach.core.isc.session.ISession;
import com.mokylin.bleach.core.msgfunc.MsgArgs;
import com.mokylin.bleach.protobuf.MessageType.MessageTarget;

public class ClientMsgFunctionService {

	private static Logger logger = LoggerFactory.getLogger(ClientMsgFunctionService.class);
	
	private ImmutableTable<MessageTarget, Integer, IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>> funcTable;
	private ImmutableMap<Integer, Parser<? extends GeneratedMessage>> paserMap;
	
	public ClientMsgFunctionService(Table<MessageTarget, Integer, IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>> funcTable, Map<Integer, Parser<? extends GeneratedMessage>> paserMap) {
		this.funcTable = ImmutableTable.copyOf(checkNotNull(funcTable, "MsgFunctionService can not init with null parsers!"));
		this.paserMap = ImmutableMap.copyOf(checkNotNull(paserMap, "MsgFunctionService can not init with null parsers!"));
	}
	
	public <Args1 extends MsgArgs, Args2 extends MsgArgs> void handle(MessageTarget messageTarget, int msgType, byte[] content, ISession session, Args1 arg1, Args2 arg2){
		IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs> msgFunc =  this.funcTable.get(messageTarget, msgType);
		Parser<? extends GeneratedMessage> parser = this.paserMap.get(msgType);
		if(msgFunc == null || parser == null) {
			logger.error(String.format("Cannot find IClientMsgFunc for message[%d]", msgType));
			return;
		}
		
		try {
			msgFunc.handle(session, parser.parseFrom(content), arg1, arg2);
		} catch (InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
	}
}
