package com.mokylin.td.network2client.core.handle;

import com.mokylin.bleach.core.net.msg.CSMessage;
import com.mokylin.td.network2client.core.session.IClientSession;

public interface IClientMessageHandler {
    void handle(IClientSession session, CSMessage msg);
}
