package com.mokylin.td.loginserver.core.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mokylin.bleach.core.concurrent.fixthreadpool.IRunnableBindId;
import com.mokylin.td.clientmsg.core.ICommunicationDataBase;
import com.mokylin.td.loginserver.globals.Globals;
import com.mokylin.td.network2client.core.session.IClientSession;
import com.mokylin.td.network2client.handle.IClientMsgHandle;

/**
 * 玩家的登陆消息都会被扔到这里来
 * @author baoliang.shen
 *
 */
public class LoginRunnable implements IRunnableBindId {
	private static Logger log = LoggerFactory.getLogger(LoginRunnable.class);

	private final IClientSession session;
	private final ICommunicationDataBase msg;

	public LoginRunnable(IClientSession session, ICommunicationDataBase msg) {
		this.session = session;
		this.msg = msg;
	}

	@Override
	public void run() {
		final int msgType = msg.getSerializationID();
		IClientMsgHandle<ICommunicationDataBase> msgHandle = Globals.getMsgHandle(msgType);
		if (msgHandle==null) {
			log.warn("msgType【" + msgType + "】 missing handle!");
			return;
		}

		msgHandle.handle(session, msg);
	}

	@Override
	public long bindId() {
		return session.getSessionId();
	}

	public IClientSession getSession() {
		return session;
	}
}
