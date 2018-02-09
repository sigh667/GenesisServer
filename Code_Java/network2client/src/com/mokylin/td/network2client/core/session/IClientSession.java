package com.mokylin.td.network2client.core.session;

import com.google.protobuf.GeneratedMessage;
import com.mokylin.bleach.core.net.msg.SCMessage;

/**
 * 用于操作和维护与客户端连接的接口。<p>
 *
 * @author pangchong
 *
 */

public interface IClientSession {

    /**
     * 给客户端发送消息
     * @param msg
     */
    void sendMessage(SCMessage msg);

    void sendMessage(GeneratedMessage msg);

    <T extends GeneratedMessage.Builder<T>> void sendMessage(GeneratedMessage.Builder<T> msg);

    /**
     * 断开与客户端的连接
     */
    void disconnect();

    /**
     * 此连接在本服中的唯一ID
     * @return
     */
    long getSessionId();

    /**
     * 此连接要登入的GameServer的ID
     * @return
     */
    int getTargetGameServerId();

    /**
     * 设置此连接要登入的GameServer的ID
     * @param gameServerId
     */
    void setTargetGameServerId(int gameServerId);

    long getUuid();

    void setUuid(long uuid);

    /**
     * 连接是否已断开
     * @return
     */
    boolean isInActive();

    /**
     * 获取客户端地址
     * @return
     */
    String getClientAddress();

    /**
     * 获取建立连接时的时间戳
     * @return
     */
    long getConnectedTime();
}
