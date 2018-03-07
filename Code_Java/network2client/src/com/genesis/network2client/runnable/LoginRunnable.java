package com.genesis.network2client.runnable;

import com.genesis.network2client.msg.ClientMsg;
import com.genesis.network2client.process.ClientMsgProcessor;
import com.genesis.network2client.session.IClientSession;
import com.mokylin.bleach.core.concurrent.fixthreadpool.IRunnableBindId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 玩家的登陆消息都会被扔到这里来
 * @author Joey
 *
 */
public class LoginRunnable implements IRunnableBindId {
    private static Logger log = LoggerFactory.getLogger(LoginRunnable.class);

    private final IClientSession session;
    private final ClientMsg msg;
    private final ClientMsgProcessor msgProcessor;

    public LoginRunnable(IClientSession session, ClientMsg msg, ClientMsgProcessor msgProcessor) {
        this.session = session;
        this.msg = msg;
        this.msgProcessor = msgProcessor;
    }

    @Override
    public void run() {
        msgProcessor.handle(msg.messageType, msg.messageContent, session);
    }

    @Override
    public long bindId() {
        return session.getSessionId();
    }

    public IClientSession getSession() {
        return session;
    }
}
