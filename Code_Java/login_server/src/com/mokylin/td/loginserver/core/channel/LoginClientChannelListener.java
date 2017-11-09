package com.mokylin.td.loginserver.core.channel;

import com.mokylin.td.loginserver.core.ClientSessionContainer;
import com.mokylin.td.network2client.core.channel.IChannelListener;
import com.mokylin.td.network2client.core.session.IClientSession;

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
