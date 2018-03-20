package com.genesis.network2client.runnable;

import com.genesis.network2client.msg.ClientMsg;
import com.genesis.network2client.process.ClientMsgProcessor;
import com.genesis.network2client.session.IClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 玩家的登陆消息都会被扔到这里来
 * @author Joey
 *
 */
public class MsgProcessRunnable extends IRunnableWithClientSession {
    private static Logger log = LoggerFactory.getLogger(MsgProcessRunnable.class);

    private final ClientMsg msg;
    private final ClientMsgProcessor msgProcessor;

    public MsgProcessRunnable(IClientSession session, ClientMsg msg, ClientMsgProcessor msgProcessor) {
        super(session);
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
}
