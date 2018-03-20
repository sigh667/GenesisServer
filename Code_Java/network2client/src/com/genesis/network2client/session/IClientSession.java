package com.genesis.network2client.session;

import com.google.protobuf.GeneratedMessage;
import com.genesis.core.net.msg.SCMessage;

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

    /**渠道*/
    void setChannel(String channel);
    String getChannel();
    /**账号ID*/
    void setAccountId(String accountId);
    String getAccountId();

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

    ///////////////////////////////////////////
    // 用来防止消息重放的自增序号，相关接口
    /**
     * 序号：用来防止客户端消息重放的一种手段
     * @return 序号是否初始化过
     */
    boolean isIndexGenerated();
    /**
     * @return 初始化序号
     */
    byte generateIndex();
    /**
     * @return 获取序号值，然后++
     */
    byte incIndexAndGet();
    ///////////////////////////////////////////
}
