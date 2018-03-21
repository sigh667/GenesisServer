package com.genesis.robot.core.msgfunc;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;

import com.genesis.core.function.FunctionUtil;
import com.genesis.core.msgfunc.exception.MessageFunctionParseException;
import com.genesis.core.msgfunc.protobufutil.MsgUtil;
import com.genesis.core.util.GenericityUtil;
import com.genesis.core.util.PackageUtil;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GCMsgFunctionUtil {

    /**
     * 这个方法服务端已经不再使用，只有机器人工程在使用，之后可能会被移动到机器人工程中去
     * @param serverMsgFuncsPackage    要扫描的包
     * @param isCG    是否扫CG消息的handler（false的话就扫GC消息）
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Pair<Map<Integer, IGCMsgFunc<GeneratedMessage>>, Map<Integer, Parser<? extends GeneratedMessage>>> buildMsgFunction(
            String serverMsgFuncsPackage) {
        Map<Integer, IGCMsgFunc<GeneratedMessage>> funcsMap = new HashMap<>();
        Map<Integer, Parser<? extends GeneratedMessage>> parserMap = new HashMap<>();

        Set<Class<?>> funcClassSet =
                PackageUtil.getSubClass(serverMsgFuncsPackage, IGCMsgFunc.class);

        if (funcClassSet == null || funcClassSet.isEmpty()) {
            return Pair.of(funcsMap, parserMap);
        }

        try {
            for (Class<?> each : funcClassSet) {
                FunctionUtil.assertIsFunctionClass(each);
                Class<?> msgTypeClass =
                        GenericityUtil.extractFirstGenericType(each, IGCMsgFunc.class);
                Integer msgType = MsgUtil.getMsgType(msgTypeClass, false);

                if (funcsMap.containsKey(msgType)) {
                    throw new RuntimeException("Multi Server Msg Function for One Message Type: " +
                            msgTypeClass.getName());
                }
                if (parserMap.containsKey(msgType)) {
                    throw new RuntimeException("Multi Server Msg Parser for One Message Type: " +
                            msgTypeClass.getName());
                }
                funcsMap.put(msgType, (IGCMsgFunc<GeneratedMessage>) each.newInstance());
                parserMap.put(msgType, MsgUtil.extractMsgParser(msgTypeClass));
            }
        } catch (Exception e) {
            throw new MessageFunctionParseException(e);
        }
        return Pair.of(funcsMap, parserMap);
    }

}

