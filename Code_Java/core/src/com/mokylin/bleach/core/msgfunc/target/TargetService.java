package com.mokylin.bleach.core.msgfunc.target;

import com.genesis.protobuf.MessageType;
import com.mokylin.bleach.core.isc.ServerType;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 查询消息号——Target的服务
 * @author: Joey
 * @create: 2018-02-08 17:50
 **/
public enum TargetService {
    Inst;

    /**<消息号, ProtoBuf中的服务器类型>*/
    private Map<Integer, MessageType.MessageTarget> map = new HashMap<>();
    /**<ProtoBuf中的服务器类型，服务器类型>*/
    private Map<MessageType.MessageTarget, ServerType> serverTypeMap = new HashMap<>();

    TargetService(){

        for (ServerType serverType : ServerType.values()) {
            if (serverType.target==null)
                continue;

            serverTypeMap.put(serverType.target, serverType);
        }

        for (MessageType.CGMessageType msgType:MessageType.CGMessageType.values()) {
            MessageType.MessageTarget target =
                    msgType.getValueDescriptor().getOptions().getExtension(MessageType.tARGET);

            if (target==null)
                continue;

            map.put(msgType.getNumber(), target);
        }
    }

    /**
     * 查询要发往哪里
     * @param msgType   消息号
     * @return 目标服务器枚举
     */
    public ServerType getServerType(Integer msgType) {
        final MessageType.MessageTarget messageTarget = map.get(msgType);
        if (messageTarget==null) {
            return null;
        } else {
            return serverTypeMap.get(messageTarget);
        }
    }
}
