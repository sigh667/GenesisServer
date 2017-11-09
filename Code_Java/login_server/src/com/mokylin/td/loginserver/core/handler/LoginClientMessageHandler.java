package com.mokylin.td.loginserver.core.handler;

import com.mokylin.bleach.core.concurrent.fixthreadpool.FixThreadPool;
import com.mokylin.td.clientmsg.core.ICommunicationDataBase;
import com.mokylin.td.loginserver.core.runnable.LoginRunnable;
import com.mokylin.td.network2client.core.handle.IClientMessageHandler;
import com.mokylin.td.network2client.core.session.IClientSession;

public class LoginClientMessageHandler implements IClientMessageHandler {

	/**固定线程池*/
	private FixThreadPool fixThreadPool;

	public LoginClientMessageHandler(FixThreadPool fixThreadPool) {
		this.fixThreadPool = fixThreadPool;
	}

	@Override
	public void handle(IClientSession session, ICommunicationDataBase msg) {
		// 此处是Netty线程在运行，将其提交到线程池处理，是为了不阻塞其他玩家的消息处理
		fixThreadPool.submit(new LoginRunnable(session, msg));
	}
}
