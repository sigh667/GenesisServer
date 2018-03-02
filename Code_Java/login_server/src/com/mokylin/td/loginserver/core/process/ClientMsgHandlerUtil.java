package com.mokylin.td.loginserver.core.process;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Parser;
import com.mokylin.bleach.core.function.FunctionUtil;
import com.mokylin.bleach.core.msgfunc.exception.MessageFunctionParseException;
import com.mokylin.bleach.core.msgfunc.protobufutil.MsgUtil;
import com.mokylin.bleach.core.util.GenericityUtil;
import com.mokylin.bleach.core.util.PackageUtil;
import com.genesis.protobuf.MessageType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handler扫描工具类
 * @author baoliang.shen
 *
 */
public class ClientMsgHandlerUtil {

    /**
     * 遍历指定包名下的所有Java文件，组装Handler
     * @param packageName    包名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Pair<Map<Integer, IClientMsgHandler<GeneratedMessage>>, Map<Integer, Parser<? extends GeneratedMessage>>> buildMsgHandlers(
            String packageName) {
        Map<Integer, IClientMsgHandler<GeneratedMessage>> funcsMap = new HashMap<>();
        Map<Integer, Parser<? extends GeneratedMessage>> parserMap = new HashMap<>();

        Set<Class<?>> funcClassSet =
                PackageUtil.getSubClass(packageName, IClientMsgHandler.class);

        if (funcClassSet == null || funcClassSet.isEmpty()) {
            return Pair.of(funcsMap, parserMap);
        }

        try {
            for (Class<?> each : funcClassSet) {
                FunctionUtil.assertIsFunctionClass(each);
                Class<?> msgTypeClass =
                        GenericityUtil.extractFirstGenericType(each, IClientMsgHandler.class);
                Integer msgTypeId = MsgUtil.getMsgType(msgTypeClass, true);
                MessageType.CGMessageType msgType = MessageType.CGMessageType.valueOf(msgTypeId);

                if (funcsMap.containsKey(msgTypeId)) {
                    throw new RuntimeException("Multi Client Msg Function for One Message Type: " +
                            msgTypeClass.getName());
                }
                if (parserMap.containsKey(msgTypeId)) {
                    throw new RuntimeException("Multi Client Msg Parser for One Message Type: " +
                            msgTypeClass.getName());
                }

                funcsMap.put(msgTypeId, (IClientMsgHandler<GeneratedMessage>) each.newInstance());
                parserMap.put(msgTypeId, MsgUtil.extractMsgParser(msgTypeClass));
            }
        } catch (Exception e) {
            throw new MessageFunctionParseException(e);
        }
        return Pair.of(funcsMap, parserMap);
    }



//    /**
//     * 查找指定包下，所有继承自{@link IClientMsgHandler}的Handler
//     * @param clisentMsgFuncsPackage 所有消息所在的包
//     * @return
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     */
//    public static Map<Integer, IClientMsgHandler<GeneratedMessage>> buildMsgHandle(
//            String clisentMsgFuncsPackage) throws InstantiationException, IllegalAccessException {
//        Map<Integer, IClientMsgHandler<GeneratedMessage>> map = new HashMap<>();
//        Set<Class<?>> funcClassSet =
//                PackageUtil.getSubClass(clisentMsgFuncsPackage, IClientMsgHandler.class);
//        if (funcClassSet == null || funcClassSet.isEmpty()) {
//            return map;
//        }
//
//        for (Class<?> each : funcClassSet) {
//            FunctionUtil.assertIsFunctionClass(each);
//            Class<?> msgTypeClass =
//                    GenericityUtil.extractFirstGenericType(each, IClientMsgHandler.class);
//            ICommunicationDataBase msgInstance =
//                    (ICommunicationDataBase) msgTypeClass.newInstance();
//            Integer msgTypeId = msgInstance.getSerializationID();
//            @SuppressWarnings("unchecked") IClientMsgHandler<GeneratedMessage> eachHandler =
//                    (IClientMsgHandler<GeneratedMessage>) each.newInstance();
//            map.put(msgTypeId, eachHandler);
//        }
//
//        return map;
//    }

}
