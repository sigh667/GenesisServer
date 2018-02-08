package com.mokylin.td.network2client.handle;

import com.mokylin.td.clientmsg.core.ICommunicationDataBase;
import com.mokylin.td.network2client.core.session.IClientSession;

public interface IClientMsgHandle<Msg extends ICommunicationDataBase> {

    void handle(IClientSession session, Msg msg);
}
