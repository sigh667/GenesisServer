package com.genesis.network2client.handle;

import com.genesis.core.net.msg.CSMessage;
import com.genesis.network2client.session.IClientSession;

/**
 * 网络消息处理器
 * <p>针对客户端发来的消息
 * <p>在netty接收到消息的时候执行
 *
 * @author yaguang.xiao
 *
 */

public interface INettyMessageHandler {

    void handle(IClientSession session, CSMessage msg);
}
