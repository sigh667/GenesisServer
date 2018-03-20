package com.genesis.network2client.runnable;

import com.genesis.network2client.session.IClientSession;
import com.genesis.core.concurrent.fixthreadpool.IRunnableBindId;

/**
 * 包含ClientSession的绑定固定ID的Runnable
 */
public abstract class IRunnableWithClientSession implements IRunnableBindId {
    protected final IClientSession session;

    public IRunnableWithClientSession(IClientSession session) {
        this.session = session;
    }

    public IClientSession getSession() {
        return session;
    }
}
