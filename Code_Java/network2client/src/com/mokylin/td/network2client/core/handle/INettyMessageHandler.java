package com.mokylin.td.network2client.core.handle;

import com.mokylin.bleach.core.net.msg.CSMessage;
import com.mokylin.td.network2client.core.session.IClientSession;

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
