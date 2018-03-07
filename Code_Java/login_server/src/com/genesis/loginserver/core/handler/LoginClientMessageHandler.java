package com.genesis.loginserver.core.handler;

import com.genesis.loginserver.globals.Globals;
import com.genesis.network2client.runnable.MsgProcessRunnable;
import com.mokylin.bleach.core.concurrent.fixthreadpool.FixThreadPool;
import com.genesis.network2client.handle.IClientMessageHandler;
import com.genesis.network2client.msg.ClientMsg;
import com.genesis.network2client.session.IClientSession;

public class LoginClientMessageHandler implements IClientMessageHandler {

    /**固定线程池*/
    private FixThreadPool fixThreadPool;

    public LoginClientMessageHandler(FixThreadPool fixThreadPool) {
        this.fixThreadPool = fixThreadPool;
    }

    @Override
    public void handle(IClientSession session, ClientMsg msg) {
        // 当前是Netty线程，将其提交到固定线程池，是为了不阻塞其他玩家的消息处理
        fixThreadPool.submit(new MsgProcessRunnable(session, msg, Globals.getClientMsgProcessor()));
    }
}
