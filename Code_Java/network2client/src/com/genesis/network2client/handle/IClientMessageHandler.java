package com.genesis.network2client.handle;

import com.genesis.network2client.msg.ClientMsg;
import com.genesis.network2client.session.IClientSession;

public interface IClientMessageHandler {
    void handle(IClientSession session, ClientMsg msg);
}
