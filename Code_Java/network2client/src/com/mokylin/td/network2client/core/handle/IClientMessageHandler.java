package com.mokylin.td.network2client.core.handle;

import com.mokylin.td.clientmsg.core.ICommunicationDataBase;
import com.mokylin.td.network2client.core.session.IClientSession;

public interface IClientMessageHandler {
	void handle(IClientSession session, ICommunicationDataBase msg);
}
