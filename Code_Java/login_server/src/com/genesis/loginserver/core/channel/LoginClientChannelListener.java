package com.genesis.loginserver.core.channel;

import com.genesis.network2client.session.ClientSessionContainer;
import com.genesis.network2client.channel.IChannelListener;
import com.genesis.network2client.session.IClientSession;

public class LoginClientChannelListener implements IChannelListener {

    @Override
    public void onChannelActive(IClientSession session) {
        ClientSessionContainer.Inst.insert(session);
    }

    @Override
    public void onChannelInActive(IClientSession session) {
        ClientSessionContainer.Inst.remove(session.getSessionId());
    }

}
