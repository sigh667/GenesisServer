package com.mokylin.bleach.core.msgfunc.target;

import com.genesis.protobuf.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 查询消息号——Target的服务
 * @author: Joey
 * @create: 2018-02-08 17:50
 **/
public enum TargetService {
    Inst;

    private Map<Integer, MessageType.MessageTarget> map = new HashMap<>();

    TargetService(){

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
     * @return
     */
    public MessageType.MessageTarget getTarget(Integer msgType) {
        return map.get(msgType);
    }
}
