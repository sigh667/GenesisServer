package com.genesis.network2client.channel;

import com.genesis.network2client.session.IClientSession;

/**
 * 网络通道监听器
 *
 * <p>此接口监听netty管道的建立事件，可以在建立连接的时候做一些操作
 *
 * @author yaguang.xiao
 *
 */

public interface IChannelListener {

    /**
     * 通道激活（连接建立）的监听方法
     * @param session
     */
    void onChannelActive(final IClientSession session);

    /**
     * 通道关闭的（连接关闭）的监听方法
     * @param session
     */
    void onChannelInActive(IClientSession session);
}
