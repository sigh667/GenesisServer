package com.genesis.loginserver.core.process;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.genesis.network2client.session.IClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description: 客户端消息分发器
 * @author: Joey
 * @create: 2018-02-08 14:27
 */
public class ClientMsgProcessor {
    // 日志
    private static Logger logger = LoggerFactory.getLogger(ClientMsgProcessor.class);

    private Map<Integer, IClientMsgHandler<GeneratedMessage>> funcMap;
    private Map<Integer, Parser<? extends GeneratedMessage>> paserMap;

    public ClientMsgProcessor(
            Map<Integer, IClientMsgHandler<GeneratedMessage>> funcTable,
            Map<Integer, Parser<? extends GeneratedMessage>> paserMap) {
        this.funcMap = checkNotNull(funcTable,"MsgFunctionService can not init with null parsers!");
        this.paserMap = checkNotNull(paserMap,"MsgFunctionService can not init with null parsers!");
    }

    public void handle(int msgType, byte[] content, IClientSession session) {
        IClientMsgHandler<GeneratedMessage> msgFunc = funcMap.get(msgType);
        Parser<? extends GeneratedMessage> parser = this.paserMap.get(msgType);
        if (msgFunc == null || parser == null) {
            logger.error(String.format("Cannot find IClientMsgHandler for message[%d]", msgType));
            return;
        }

        try {
            msgFunc.handle(session, parser.parseFrom(content));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
