package com.genesis.core.msgfunc.client;

import com.genesis.core.msgfunc.MsgArgs;
import com.genesis.core.msgfunc.protobufutil.MsgUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;

import com.genesis.core.function.FunctionUtil;
import com.genesis.core.msgfunc.exception.MessageFunctionParseException;
import com.genesis.core.util.GenericityUtil;
import com.genesis.core.util.PackageUtil;
import com.genesis.protobuf.MessageType;
import com.genesis.protobuf.MessageType.CGMessageType;
import com.genesis.protobuf.MessageType.MessageTarget;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClientMsgFunctionUtil {

    /**
     * 遍历指定包名下的所有Java文件，组装Handler
     * @param clisentMsgFuncsPackage    包名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Pair<Table<MessageTarget, Integer, IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>>, Map<Integer, Parser<? extends GeneratedMessage>>> buildMsgFunction(
            String clisentMsgFuncsPackage) {
        Table<MessageTarget, Integer, IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>>
                funcsTable = HashBasedTable.create();
        Map<Integer, Parser<? extends GeneratedMessage>> parserMap = new HashMap<>();

        Set<Class<?>> funcClassSet =
                PackageUtil.getSubClass(clisentMsgFuncsPackage, IClientMsgFunc.class);

        if (funcClassSet == null || funcClassSet.isEmpty()) {
            return Pair.of(funcsTable, parserMap);
        }

        try {
            for (Class<?> each : funcClassSet) {
                FunctionUtil.assertIsFunctionClass(each);
                Class<?> msgTypeClass =
                        GenericityUtil.extractFirstGenericType(each, IClientMsgFunc.class);
                Integer msgTypeId = MsgUtil.getMsgType(msgTypeClass, true);
                CGMessageType msgType = CGMessageType.valueOf(msgTypeId);
                MessageTarget target =
                        msgType.getValueDescriptor().getOptions().getExtension(MessageType.tARGET);

                if (funcsTable.contains(target, msgTypeId)) {
                    throw new RuntimeException("Multi Client Msg Function for One Message Type: " +
                            msgTypeClass.getName());
                }
                if (parserMap.containsKey(msgTypeId)) {
                    throw new RuntimeException("Multi Client Msg Parser for One Message Type: " +
                            msgTypeClass.getName());
                }

                funcsTable.put(target, msgTypeId,
                        (IClientMsgFunc<GeneratedMessage, MsgArgs, MsgArgs>) each.newInstance());
                parserMap.put(msgTypeId, MsgUtil.extractMsgParser(msgTypeClass));
            }
        } catch (Exception e) {
            throw new MessageFunctionParseException(e);
        }
        return Pair.of(funcsTable, parserMap);
    }

}
