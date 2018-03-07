package com.genesis.gateserver.core.net.handlers;

import com.genesis.gateserver.global.Globals;
import com.mokylin.bleach.core.concurrent.fixthreadpool.IActionOnException;
import com.mokylin.bleach.core.isc.ServerType;
import com.mokylin.bleach.core.msgfunc.target.TargetService;
import com.genesis.network2client.handle.IClientMessageHandler;
import com.genesis.network2client.msg.ClientMsg;
import com.genesis.network2client.session.IClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关的客户端消息处理器
 *
 * <p>
 * 这里面的{@link #handle(IClientSession, ClientMsg)}方法在netty的线程中执行<br>
 * 在netty中选用的线程策略是来自同一个客户端的消息必然在相同的线程中处理，来自不同客户端的消息不一定在相同的线程中处理
 *
 * @author Joey
 *
 */
public class AgentClientMessageHandler implements IClientMessageHandler {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AgentClientMessageHandler.class);

    @Override
    public void handle(IClientSession session, ClientMsg msg) {

        // 需要区分出哪些消息要转发
        final ServerType serverType = TargetService.Inst.getServerType(msg.messageType);
        switch (serverType) {
            case GAME:
                // TODO
                break;
            // 未来还会添加其他服的消息转发
            default:
                // 默认为本服处理
                Globals.getClientMsgProcessor().handle(msg.messageType, msg.messageContent, session);
                break;
        }
    }

}
