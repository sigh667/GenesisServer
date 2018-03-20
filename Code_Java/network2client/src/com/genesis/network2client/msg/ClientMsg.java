package com.genesis.network2client.msg;

import com.genesis.core.net.msg.BaseMessage;

/**
 * 客户端发来的消息
 * <p>2018-03-02 18:04
 *
 * @author Joey
 **/
public class ClientMsg extends BaseMessage {

    /**自增序列值（用于防止消息重放）*/
    public byte index = 0;

    public ClientMsg() {
        super();
    }

    public ClientMsg(int messageType, byte[] messageContent, byte index) {
        super(messageType, messageContent);
        this.index = index;
    }
}
