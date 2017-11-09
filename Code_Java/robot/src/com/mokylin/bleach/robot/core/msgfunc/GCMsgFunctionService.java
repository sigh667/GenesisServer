package com.mokylin.bleach.robot.core.msgfunc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.mokylin.bleach.core.isc.session.ISession;

/**
 * 消息解析的映射
 * @author yaguang.xiao
 *
 */
public class GCMsgFunctionService {
	
	private static final Logger log = LoggerFactory.getLogger(GCMsgFunctionService.class);
	
	private ImmutableMap<Integer, IGCMsgFunc<GeneratedMessage>> funcMap;
	private ImmutableMap<Integer, Parser<? extends GeneratedMessage>> paserMap;
	
	public GCMsgFunctionService(Map<Integer, IGCMsgFunc<GeneratedMessage>> funcs, Map<Integer, Parser<? extends GeneratedMessage>> parsers){
		funcMap = ImmutableMap.copyOf(checkNotNull(funcs, "MsgFunctionService can not init with null funcs!"));
		paserMap = ImmutableMap.copyOf(checkNotNull(parsers, "MsgFunctionService can not init with null parsers!"));
	}
	
	public void handle(int msgType, byte[] content, ISession session){
		try {
			IGCMsgFunc<GeneratedMessage> igcMsgFunc = funcMap.get(msgType);
			if (igcMsgFunc==null) {
				log.debug("消息号【{}】没有对应的解析方法", msgType);
				return;
			}

			igcMsgFunc.handle(session, paserMap.get(msgType).parseFrom(content));
		} catch (InvalidProtocolBufferException e) {
			throw new RuntimeException(e);
		}
	}
}
