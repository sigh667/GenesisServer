package com.genesis.loginserver.core.runnable;

import com.mokylin.bleach.core.concurrent.fixthreadpool.IRunnableBindId;
import com.mokylin.bleach.core.net.msg.CSMessage;
import com.genesis.loginserver.globals.Globals;
import com.mokylin.td.network2client.core.session.IClientSession;
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
    private final CSMessage msg;

    public LoginRunnable(IClientSession session, CSMessage msg) {
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
