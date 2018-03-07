package com.genesis.loginserver.core.runnable;

import com.genesis.loginserver.globals.Globals;
import com.mokylin.bleach.core.concurrent.fixthreadpool.IRunnableBindId;
import com.genesis.network2client.msg.ClientMsg;
import com.genesis.network2client.session.IClientSession;
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

    public LoginRunnable(IClientSession session, ClientMsg msg) {
        this.session = session;
        this.msg = msg;
    }

    @Override
    public void run() {
        Globals.getClientMsgProcessor().handle(msg.messageType, msg.messageContent, session);
    }

    @Override
    public long bindId() {
        return session.getSessionId();
    }

    public IClientSession getSession() {
        return session;
    }
}
